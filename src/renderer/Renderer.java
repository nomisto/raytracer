package renderer;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import global.GLOBAL;
import poisson.PoissonSampler;
import scene.*;
import scene.light.*;
import scene.material.*;
import scene.surface.*;
import util.TextureBuffer;
import util.Vec2;
import util.Vec3;
import util.Vec4;

/**
 * @author Simon
 * The class renderer contains all functions that are essential for raytracing
 * and saving the image
 */
public class Renderer {
	
	
	Scene s;
	int[] pixels;
	int height, width;
	
	boolean ss = false;
	double poissonHeight,poissonWidth,minimum;
	
	/**
	 * @param s Scene which needs to be rendered
	 * Constructor for class Renderer, initializes array which represents the output image.
	 */
	public Renderer(Scene s){
		this.s = s;
		height = s.getCamera().getResolution()[0];
		width = s.getCamera().getResolution()[1];
		pixels = new int[width*height];
	}
	
	public Renderer(Scene s, double poissonWidth, double poissonHeight, double minimum){
		this.s = s;
		height = s.getCamera().getResolution()[0];
		width = s.getCamera().getResolution()[1];
		pixels = new int[width*height];
		this.poissonHeight = poissonHeight;
		this.poissonWidth = poissonWidth;
		this.minimum = minimum;
		ss = true;
	}
	
	
	
	/**
	 * Main function of this class
	 * Traces for each pixel of the image the outgoing color
	 */
	public void render(){
		ArrayList<Surface> surfaceList = s.getSurfaces();
			for (int y=0; y<height; y++){
				for(int x=0; x<width; x++){
					if(!ss){
						Ray ray = s.getCamera().constructEyeRay(x,y);
						Vec3 color = trace(ray,surfaceList,0);
						pixels[y*width + x] = colorToIntRGB(color);
					} else{
						PoissonSampler ps = new PoissonSampler(poissonWidth,poissonHeight,minimum);
						double size = 0;
						double r = 0;
						double g = 0;
						double b = 0;
						for(Vec2 p : ps.getSampler()){
							double newX = x + p.getX();
							double newY = y + p.getY();
							if(newX>=0 && newY>=0 && newX<width && newY<height){
								Ray ray = s.getCamera().constructEyeRay(newX,newY);
								Vec3 color = trace(ray,surfaceList,0);
								r = r+ color.getX();
								g = g + color.getY();
								b = b + color.getZ();
								size++;
							}
						}
						r = r/size;
						g = g/size;
						b = b/size;
						Vec3 avgColor = new Vec3(r,g,b);
						pixels[y*width + x] = colorToIntRGB(avgColor);
					}
				}
			}
		
		
		try {
			getImageFromArray(pixels,width,height,s.getOutputfile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	/**
	 * Traces the ray, by calling the intersectionTest of the Sphere/Mesh.
	 * Calls the shade function for the Surface which is the nearest.
	 * @param ray Contains the origin and the direction of the viewing ray
	 * @param surfaceList All surfaces in the scene
	 * @return Color of the pixel intersected by the ray
	 */
	public Vec3 trace(Ray ray,ArrayList<Surface> surfaceList,int depth){
		Ray r = ray.addEpsilon();
		Intersection nearest = null;
		Double t = Double.MAX_VALUE;
		Vec3 color;
		Vec3 colorTrans = new Vec3(0,0,0);
		Vec3 colorRef = new Vec3(0,0,0);
		for(Surface surf:surfaceList){
			Intersection current = null;
			if(surf instanceof Sphere){
				current = r.getTransformedRay(surf).intersectionTestSphere((Sphere) surf);
			} else if(surf instanceof Mesh){
				current = r.getTransformedRay(surf).intersectionTestMesh((Mesh) surf);
			}
			if(current!=null && current.getT()>0 && current.getT()<t){
				nearest = current;
				t=current.getT();
			}
			
		}
		if(nearest!=null){
			Vec3 intersecP = r.getIP(t);
			Vec3 normal = null;
			if(nearest.getSurf() instanceof Sphere){
				normal = calculateSphereNormal(nearest, r.getTransformedRay(nearest.getSurf()).getIP(t));
			} else if(nearest.getSurf() instanceof Mesh){
				normal = calculateMeshNormal(nearest);
			}
			color = shade(r,nearest,intersecP,normal);
			
			if (depth>=s.getCamera().getMax_bounces()){
				return color;
			}
			double trans = nearest.getSurf().getMaterial().getTransmittance();
			double ref = nearest.getSurf().getMaterial().getReflactance();
			double iof = nearest.getSurf().getMaterial().getRefraction();
			
			if (trans>0){
				Vec3 transDir = r.calculateTransmittanceDirection(normal,iof);
				if(transDir==null) {
					transDir = r.calculateReflectanceDirection(normal);
					color = new Vec3(0,0,0);
				}
				Ray transRay = new Ray(intersecP, transDir);
				colorTrans = trace(transRay,surfaceList,depth+1);
				
			}
			if(ref>0){
				Ray refRay = new Ray(intersecP, r.calculateReflectanceDirection(normal));
				colorRef = trace(refRay,surfaceList,depth+1);
			}
			colorTrans = colorTrans.multiplyWithScalar(trans);
			colorRef = colorRef.multiplyWithScalar(ref);
			color = color.multiplyWithScalar(1-trans-ref);
			return colorTrans.addWith(colorRef.addWith(color));
		} else {
			return s.getBackground_color();
		}
		
		
	}
	
	
	
	
	/**
	 * Calculates the Sphere normal, by calculating the vector origin of the sphere -> intersectionpoint
	 * @param nearest The sphere intersecting the ray
	 * @param intersecP The intersection point of the sphere and the ray
	 * @return Vec3 containing the normal
	 */
	public Vec3 calculateSphereNormal(Intersection nearest,Vec3 intersecP){
		Vec3 nearestPos = ((Sphere) nearest.getSurf()).getPosition();
		Vec3 normal = intersecP.subtractedWith(nearestPos);
		normal = nearest.getSurf().getTransformationMatrix().transpose().multiplyNormal(normal);
		normal = normal.normalize();
		return normal;
	}
	
	
	
	/**
	 * Calculates the Mesh normal by interpolation between the normals of the points of the triangle
	 * @param nearest Mesh intersecting the ray
	 * @return Vec3 containing the normal
	 */
	public Vec3 calculateMeshNormal(Intersection nearest){
		ArrayList<Vec3> normals = ((Mesh) nearest.getSurf()).getObj().getNormals();
		
		double beta = nearest.getBeta();
		double gamma = nearest.getGamma();
		double alpha = beta+gamma;
		
		Vec3 normal1 = normals.get((int) (nearest.getTri().getVertex1().getZ()-1));
		Vec3 normal2 = normals.get((int) (nearest.getTri().getVertex2().getZ()-1));
		Vec3 normal3 = normals.get((int) (nearest.getTri().getVertex3().getZ()-1));
		
		Vec3 na = normal1.multiplyWithScalar(alpha);
		Vec3 nb = normal2.multiplyWithScalar(1-alpha);
		Vec3 nc = normal3.multiplyWithScalar(1-alpha);
		
		Vec3 nd = na.addWith(nb);
		Vec3 ne = na.addWith(nc);
		nd = nd.multiplyWithScalar(beta/alpha);
		ne = ne.multiplyWithScalar(gamma/alpha);
		Vec3 normal = nd.addWith(ne);
		normal = nearest.getSurf().getTransformationMatrix().transpose().multiplyVector(normal);
		normal = normal.normalize();
		return normal;
	}
	
	
	
	
	/**
	 * Shades the color of the pixel
	 * @param ray Viewing ray containing origin and direction
	 * @param nearest The nearest surface in the ray
	 * @param intersecP Intersection point of surface and ray
	 * @param normal Normal of the intersectionP
	 * @return Vec3 containing the color to shade to
	 */
	public Vec3 shade(Ray ray,Intersection nearest, Vec3 intersecP, Vec3 normal){
		Material mat = nearest.getSurf().getMaterial();
		Vec3 shadedColor = new Vec3(0,0,0);
		Vec3 materialColor = null;
		if(mat instanceof SolidMaterial){
			materialColor = ((SolidMaterial) mat).getColor();
		}else if(mat instanceof TexturedMaterial){
			if(nearest.getSurf() instanceof Mesh){
				materialColor = getMeshTextureColor(nearest,((TexturedMaterial) mat).getTexture());
			} else {
				//TBD Texture of sphere
			}
		}
		Vec4 phong = mat.getPhong();
		
		double ir = 0;
		double ig = 0;
		double ib = 0;
		
		double irs = 0;
		double igs = 0;
		double ibs = 0;
		
		for(Light l : s.getLights()){
			Vec3 lightcolor = l.getColor();
			if(l instanceof AmbientLight){
				ir = ir + lightcolor.getX() * phong.getX();
				ig = ig + lightcolor.getY() * phong.getX();
				ib = ib + lightcolor.getZ() * phong.getX();
			} else {
				if(!checkShadow(l,intersecP,nearest)){
					Vec3 li = null;
					Vec3 r;
					Vec3 v;
					if(l instanceof ParallelLight){
						li = ((ParallelLight) l).getDirection();
						li = li.normalize();
						li = li.negate();
						li = li.normalize();
					} else if(l instanceof PointLight) {
						li = ((PointLight) l).getPosition();
						li = li.subtractedWith(intersecP);
						li = li.normalize();
					}
					double ldotn = li.dotproductWith(normal);
					r = normal.multiplyWithScalar(2*ldotn);
					r = r.subtractedWith(li);
					r = r.normalize();
					v = ray.getDirection();
					v = v.normalize();
					v = v.negate();
					v = v.normalize();
					if(0<Math.acos(ldotn) && Math.acos(ldotn)<Math.PI/2){
						ir = ir + lightcolor.getX() * phong.getY() * Math.max(ldotn, 0);
						ig = ig + lightcolor.getY() * phong.getY() * Math.max(ldotn, 0);
						ib = ib + lightcolor.getZ() * phong.getY() * Math.max(ldotn, 0);
						irs = irs + lightcolor.getX() * phong.getZ() * Math.max(Math.pow(r.dotproductWith(v),phong.getW()), 0);
						igs = igs + lightcolor.getY() * phong.getZ() * Math.max(Math.pow(r.dotproductWith(v),phong.getW()), 0);
						ibs = ibs + lightcolor.getZ() * phong.getZ() * Math.max(Math.pow(r.dotproductWith(v),phong.getW()), 0);
					}
				}
			}
		}
		
		Vec3 intensity = new Vec3(ir,ig,ib);
		Vec3 color = materialColor.multiplyWithScalarVector(intensity);
	    shadedColor = shadedColor.addWith(color);
	    Vec3 intensitySpecular = new Vec3(irs,igs,ibs);
	    shadedColor = shadedColor.addWith(intensitySpecular);
		return shadedColor;
	}
	
	
	
	
	TextureBuffer tex = new TextureBuffer();
	/**
	 * Calculates the color of the intersection point, if textured
	 * @param nearest Mesh which is the nearest in the ray
	 * @param texture Name of the texture file
	 * @return An array of doubles containing the r,g and b of the color
	 */
	public Vec3 getMeshTextureColor(Intersection nearest,String texture){
		tex.initialize(texture);
		int height = tex.getHeight();
		int width = tex.getWidth();
		ArrayList<Vec2> texcoords = ((Mesh) nearest.getSurf()).getObj().getTexcoords();
		Vec2 tex1 = texcoords.get((int) (nearest.getTri().getVertex1().getY()-1));
		Vec2 tex2 = texcoords.get((int) (nearest.getTri().getVertex2().getY()-1));
		Vec2 tex3 = texcoords.get((int) (nearest.getTri().getVertex3().getY()-1));
		double beta = nearest.getBeta();
		double gamma = nearest.getGamma();
		double x = tex1.getX() + beta * (tex2.getX()-tex1.getX()) + gamma * (tex3.getX()-tex1.getX());
		double y = tex1.getY() + beta * (tex2.getY()-tex1.getY()) + gamma * (tex3.getY()-tex1.getY());
		
		x = (x%1)*(width-1);
		y = (y%1)*(height-1);
		
		//Perform bilinear interpolation
		double ceilX = Math.ceil(x);
		double ceilY = Math.ceil(y);
		double floorX = Math.floor(x);
		double floorY = Math.floor(y);
		
		double deltaX = x - floorX;
		double deltaY = y - floorY;
		
		double[] topleft = tex.getPixel((int)floorX, (int)floorY);
		double[] topright = tex.getPixel((int)ceilX, (int)floorY);
		double[] bottomleft = tex.getPixel((int)floorX,(int)ceilY);
		double[] bottomright = tex.getPixel((int)ceilX, (int)ceilY);
		
		
		double interpTopR = (1-deltaX) * topleft[0] + deltaX * topright[0];
		double interpBottomR = (1-deltaX) * bottomleft[0] + deltaX * bottomright[0];
		double interpR = (1-deltaY) * interpTopR + deltaY * interpBottomR;
		
		double interpTopG = (1-deltaX) * topleft[1] + deltaX * topright[1];
		double interpBottomG = (1-deltaX) * bottomleft[1] + deltaX * bottomright[1];
		double interpG = (1-deltaY) * interpTopG + deltaY * interpBottomG;
		
		double interpTopB = (1-deltaX) * topleft[2] + deltaX * topright[2];
		double interpBottomB = (1-deltaX) * bottomleft[2] + deltaX * bottomright[2];
		double interpB = (1-deltaY) * interpTopB + deltaY * interpBottomB;
		
		return new Vec3(interpR/255.0,interpG/255.0,interpB/255.0);
	}
	
	
	
	
	/**
	 * Checks if lightsource is blocked by another object
	 * @param l light
	 * @param iP intersection point of the viewing ray and the object
	 * @param nearest Surface 
	 * @return true if lightsource is blocked, false if ligtsource is unblocked
	 */
	public boolean checkShadow(Light l, Vec3 iP, Intersection nearest){
		ArrayList<Surface> surfaceList = s.getSurfaces();
		for(Surface s : surfaceList){
				Ray ray;
				if(l instanceof ParallelLight){
					ray = new Ray(iP, ((ParallelLight) l).getDirection().negate());
					ray = ray.addEpsilon();
					double t = 0;
					if(s instanceof Sphere){
						t = ray.getTransformedRay(s).intersectionTestSphere((Sphere)s).getT();
					} else if(s instanceof Mesh){
						t = ray.getTransformedRay(s).intersectionTestMesh((Mesh)s).getT();
					}
					if(t>0){
						return true;
					}
				} else {
					ray = new Ray(iP, ((PointLight) l).getPosition().subtractedWith(iP));
					ray = ray.addEpsilon();
					double t = 0;
					if(s instanceof Sphere){
						t = ray.getTransformedRay(s).intersectionTestSphere((Sphere)s).getT();
					} else if(s instanceof Mesh){
						t = ray.getTransformedRay(s).intersectionTestMesh((Mesh)s).getT();
					}
				
					if(t>0 && t<1){
						return true;
					}
				}
				
				
		}
		return false;
	}
	
	
	
	
	/**
	 * Converts Vec3 R,G,B into a single an Integer of Type Int_RGB
	 * meaning an 24-bit integer (8bit red, 8bit green, 8bit blue)
	 * @param color Vec3 of the color which has to be converted
	 * @return int of the color in type_int_RGB
	 */
	public static int colorToIntRGB(Vec3 color){
		if (color.getX()>1){
			color.setX(1);
		}
		if (color.getY()>1){
			color.setY(1);
		}
		if (color.getZ()>1){
			color.setZ(1);
		}
		int red = (int) (Math.round(color.getX() * 255) * Math.pow(256,2));
		int green = (int) (Math.round(color.getY() * 255) * 256);
		int blue = (int) (Math.round(color.getZ() * 255));
		return red + green + blue;
	}

	
	
	/**
	 * Creates a new BufferedImage and writes it to the file, specified in the XML
	 * @param pixels Array of the pixels calculated
	 * @param width Width of the image
	 * @param height Height of the Image
	 * @param link Name of the outputfile
	 * @throws IOException
	 */
	public static void getImageFromArray(int[] pixels, int width, int height, String link) throws IOException {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        image.setRGB(0,0,width,height,pixels,0,width);
		File outputfile = new File(GLOBAL.path + "/" + link);
		ImageIO.write(image, "png", outputfile);
    }
}
