package renderer;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import global.GLOBAL;
import obj.OBJParser;
import obj.Triangle;
import scene.*;
import scene.light.*;
import scene.material.*;
import scene.surface.*;
import util.Mat3;
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
	
	
	
	
	/**
	 * Main function of this class
	 * Traces for each pixel of the image the outgoing color
	 */
	public void render(){
		ArrayList<Surface> surfaceList = s.getSurfaces();
		for (int y=0; y<height; y++){
			for(int x=0; x<width; x++){
				Vec3[] ray = s.getCamera().constructEyeRay(x,y);
				Vec3 color = trace(ray,surfaceList);
				pixels[y*width + x] = colorToIntRGB(color);
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
	public Vec3 trace(Vec3[] ray,ArrayList<Surface> surfaceList){
		Surface nearest = null;
		Double t = Double.MAX_VALUE;
		for(Surface surf:surfaceList){
			if(surf instanceof Sphere){
				double currentT = intersectionTestSphere(((Sphere) surf).getPosition(),((Sphere) surf).getRadius(),ray);
				if(currentT>0 && currentT<t){
					nearest = surf;
					t=currentT;
				}
			} else if(surf instanceof Mesh){
				double currentT = intersectionTestMesh((Mesh)surf,((Mesh) surf).getObj(),ray,true);
				if(currentT>0 && currentT<t){
					nearest = surf;
					t = currentT;
				}
			}
		}
		if(nearest!=null){
			Vec3 intersecP = ray[0].addWith(ray[1].multiplyWithScalar(t));
			Vec3 normal = null;
			if(nearest instanceof Sphere){
				normal = calculateSphereNormal((Sphere) nearest, intersecP);
			} else if(nearest instanceof Mesh){
				normal = calculateMeshNormal((Mesh) nearest);
			}
			return shade(ray,nearest,intersecP,normal);
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
	public Vec3 calculateSphereNormal(Sphere nearest,Vec3 intersecP){
		Vec3 nearestPos = ((Sphere) nearest).getPosition();
		Vec3 normal = intersecP.subtractedWith(nearestPos);
		normal = normal.normalize();
		return normal;
	}
	
	
	
	/**
	 * Calculates the Mesh normal by interpolation between the normals of the points of the triangle
	 * @param nearest Mesh intersecting the ray
	 * @return Vec3 containing the normal
	 */
	public Vec3 calculateMeshNormal(Mesh nearest){
		ArrayList<Vec3> normals = ((Mesh) nearest).getObj().getNormals();
		
		double beta = ((Mesh) nearest).getNearestbeta();
		double gamma = ((Mesh) nearest).getNearestgamma();
		double alpha = beta+gamma;
		
		Vec3 normal1 = normals.get((int) (((Mesh) nearest).getNearest1().getZ()-1));
		Vec3 normal2 = normals.get((int) (((Mesh) nearest).getNearest2().getZ()-1));
		Vec3 normal3 = normals.get((int) (((Mesh) nearest).getNearest3().getZ()-1));
		
		Vec3 na = normal1.multiplyWithScalar(alpha);
		Vec3 nb = normal2.multiplyWithScalar(1-alpha);
		Vec3 nc = normal3.multiplyWithScalar(1-alpha);
		
		Vec3 nd = na.addWith(nb);
		Vec3 ne = na.addWith(nc);
		nd = nd.multiplyWithScalar(beta/alpha);
		ne = ne.multiplyWithScalar(gamma/alpha);
		
		return nd.addWith(ne);
	}
	
	
	
	
	
	/**
	 * Calculates if a sphere is intersecting the ray
	 * If so, returns the distance
	 * @param c the origin of the sphere
	 * @param r the radius of the sphere
	 * @param ray Calculates if a sphere is intersecting the ray
	 * If so, returns the distance
	 * @return the distance t to the sphere or 0 if ray doesn't intersect
	 */
	public double intersectionTestSphere(Vec3 c, double r, Vec3[] ray){
		Vec3 e = ray[0];
		Vec3 d = ray[1];
		Vec3 eminc = e.subtractedWith(c);
		double discriminant = Math.pow(d.dotproductWith(eminc),2) - d.dotproductWith(d)*((eminc.dotproductWith(eminc))-Math.pow(r, 2));
		if(discriminant==0){
			return (-d.dotproductWith(e.subtractedWith(c)))/(d.dotproductWith(d));
		} else if(discriminant>0){
			discriminant = Math.sqrt(discriminant);
			double plus = (-d.dotproductWith(e.subtractedWith(c))+discriminant)/(d.dotproductWith(d));
			double minus = (-d.dotproductWith(e.subtractedWith(c))-discriminant)/(d.dotproductWith(d));
			if(plus>minus && minus>0){
				return minus;
			} else if(minus>plus && plus>0){
				return plus;
			}
		}
		return 0;
	}
	
	
	
	
	/**
	 * Calculates if a Mesh is intersecting the ray
	 * If so, returns the distance
	 * @param m Mesh to test to
	 * @param obj the OBJParser Object of the Mesh
	 * @param ray Calculates if a sphere is intersecting the ray
	 * @param set Determines if the beta,gamma, and the three vertices needs to be set in Nearest
	 * @return the distance t to the Mesh or 0 if the ray doesn't intersect
	 */
	public double intersectionTestMesh(Mesh m, OBJParser obj, Vec3[] ray, boolean set){
		ArrayList<Triangle> triangles = obj.getTriangles();
		ArrayList<Vec3> vertices = obj.getVertices();
		double t = Double.MAX_VALUE;
		for(Triangle tri:triangles){
			Vec3 a = vertices.get((int) (tri.getVertex1().getX()-1));
			Vec3 b = vertices.get((int) (tri.getVertex2().getX()-1));
			Vec3 c = vertices.get((int) (tri.getVertex3().getX()-1));
			
			double beta, gamma;
			
			Mat3 M = new Mat3();
			M.fromValues(a.getX()-b.getX(),a.getY()-b.getY(),a.getZ()-b.getZ(),a.getX()-c.getX(),a.getY()-c.getY(),a.getZ()-c.getZ(),ray[1].getX(),ray[1].getY(),ray[1].getZ());
			double mdet = M.getDeterminant();
			
			Mat3 Mbeta = new Mat3();
			Mbeta.fromValues(a.getX()-ray[0].getX(),a.getY()-ray[0].getY(),a.getZ()-ray[0].getZ(),a.getX()-c.getX(),a.getY()-c.getY(),a.getZ()-c.getZ(),ray[1].getX(),ray[1].getY(),ray[1].getZ());
			double betadet = Mbeta.getDeterminant();
			beta = betadet/mdet;
			if(beta>0){
				Mat3 Mgamma = new Mat3();
				Mgamma.fromValues(a.getX()-b.getX(),a.getY()-b.getY(),a.getZ()-b.getZ(),a.getX()-ray[0].getX(),a.getY()-ray[0].getY(),a.getZ()-ray[0].getZ(),ray[1].getX(),ray[1].getY(),ray[1].getZ());
				double gammadet = Mgamma.getDeterminant();
				gamma = gammadet/mdet;
				if(gamma>0 && gamma+beta <1){
					Mat3 Mt = new Mat3();
					Mt.fromValues(a.getX()-b.getX(),a.getY()-b.getY(),a.getZ()-b.getZ(),a.getX()-c.getX(),a.getY()-c.getY(),a.getZ()-c.getZ(),a.getX()-ray[0].getX(),a.getY()-ray[0].getY(),a.getZ()-ray[0].getZ());
					double tdet = Mt.getDeterminant();
					if((tdet/mdet)<t && t>0){
						t = tdet/mdet;
						if(set){
							m.setNearest1(tri.getVertex1());
							m.setNearest2(tri.getVertex2());
							m.setNearest3(tri.getVertex3());
							m.setNearestbeta(beta);
							m.setNearestgamma(gamma);
						}
					}
				}
			}
		}
		if(t == Double.MAX_VALUE){
			return 0;
		} else {
			return t;
		}
	}
	
	
	
	
	/**
	 * Shades the color of the pixel
	 * @param ray Viewing ray containing origin and direction
	 * @param nearest The nearest surface in the ray
	 * @param intersecP Intersection point of surface and ray
	 * @param normal Normal of the intersectionP
	 * @return Vec3 containing the color to shade to
	 */
	public Vec3 shade(Vec3[] ray,Surface nearest, Vec3 intersecP, Vec3 normal){
		Material mat = nearest.getMaterial();
		Vec3 shadedColor = new Vec3(0,0,0);
		Vec3 materialColor = null;
		if(mat instanceof SolidMaterial){
			materialColor = ((SolidMaterial) mat).getColor();
		}else if(mat instanceof TexturedMaterial){
			if(nearest instanceof Mesh){
				double[] color = getMeshTextureColor((Mesh) nearest,((TexturedMaterial) mat).getTexture());
				materialColor = new Vec3(color[0]/255.0,color[1]/255.0,color[2]/255.0);
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
					v = ray[1];
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
	public double[] getMeshTextureColor(Mesh nearest,String texture){
		tex.initialize(texture);
		int height = tex.getHeight();
		int width = tex.getWidth();
		ArrayList<Vec2> texcoords = ((Mesh) nearest).getObj().getTexcoords();
		Vec2 tex1 = texcoords.get((int) (((Mesh) nearest).getNearest1().getY()-1));
		Vec2 tex2 = texcoords.get((int) (((Mesh) nearest).getNearest2().getY()-1));
		Vec2 tex3 = texcoords.get((int) (((Mesh) nearest).getNearest3().getY()-1));
		double beta = ((Mesh) nearest).getNearestbeta();
		double gamma = ((Mesh) nearest).getNearestgamma();
		Double x = tex1.getX() + beta * (tex2.getX()-tex1.getX()) + gamma * (tex3.getX()-tex1.getX());
		Double y = tex1.getY() + beta * (tex2.getY()-tex1.getY()) + gamma * (tex3.getY()-tex1.getY());
		int imgX = (int) Math.round(x*(width-1));
		int imgY = (int) Math.round(y*(height-1));
		return tex.getPixel(imgX, imgY);
	}
	
	
	
	
	/**
	 * Checks if lightsource is blocked by another object
	 * @param l light
	 * @param iP intersection point of the viewing ray and the object
	 * @param nearest Surface 
	 * @return true if lightsource is blocked, false if ligtsource is unblocked
	 */
	public boolean checkShadow(Light l, Vec3 iP, Surface nearest){
		ArrayList<Surface> surfaceList = s.getSurfaces();
		for(Surface s : surfaceList){
			if(!s.equals(nearest)){
				Vec3[] ray = new Vec3[2];
				if(l instanceof ParallelLight){
					ray[0] = iP;
					ray[1] = ((ParallelLight) l).getDirection().negate();
				} else {
					ray[0] = iP;
					ray[1] = ((PointLight) l).getPosition().subtractedWith(iP);
				}
				if(s instanceof Sphere){
					if(intersectionTestSphere(((Sphere) s).getPosition(),((Sphere) s).getRadius(),ray)>0){
						return true;
					}
				} else if(s instanceof Mesh){
					if(intersectionTestMesh((Mesh) s,((Mesh) s).getObj(),ray,false)>0){
						return true;
					}
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
