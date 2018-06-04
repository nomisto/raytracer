package java_raytracer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import scene.*;
import scene.light.*;
import scene.material.*;
import scene.surface.*;
import util.Vec3;
import util.Vec4;

public class Renderer {
	
	Scene s;
	int[] pixels;
	int height, width;
	
	public Renderer(Scene s){
		this.s = s;
		height = s.getCamera().getResolution()[0];
		width = s.getCamera().getResolution()[1];
		pixels = new int[width*height];
	}
	
	
	public void render(){
		ArrayList<Surface> surfaceList = s.getSurfaces();
		System.out.println(surfaceList.size());
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
				//TBD
			}
		}
		if(nearest!=null){
			Vec3 intersecP = ray[0].addWith(ray[1].multiplyWithScalar(t));
			Vec3 normal = null;
			if(nearest instanceof Sphere){
				Vec3 nearestPos = ((Sphere) nearest).getPosition();
				normal = intersecP.subtractedWith(nearestPos);
				normal = normal.normalize();
			} else if(nearest instanceof Mesh){
				//TBD
			}
			return shade(ray,nearest,intersecP,normal);
		} else {
			return s.getBackground_color();
		}
	}
	
	
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
	
	
	
	public Vec3 shade(Vec3[] ray,Surface nearest, Vec3 intersecP, Vec3 normal){
		Material mat = nearest.getMaterial();
		Vec3 shadedColor = new Vec3(0,0,0);
		Vec3 materialColor = null;
		
		if(mat instanceof SolidMaterial){
			materialColor = ((SolidMaterial) mat).getColor();
		}else if(mat instanceof TexturedMaterial){
			//TBD
		}
		
		
		Vec4 phong = mat.getPhong();
		for(Light l : s.getLights()){
			
			double ir = 0;
			double ig = 0;
			double ib = 0;

			Vec3 lightcolor = l.getColor();
			if(l instanceof AmbientLight){
				ir = ir + lightcolor.getX() * phong.getX();
				ig = ig + lightcolor.getY() * phong.getX();
				ib = ib + lightcolor.getZ() * phong.getX();
			} else {
				
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
					ir = ir + lightcolor.getX() * phong.getZ() * Math.max(Math.pow(r.dotproductWith(v),phong.getW()), 0);
					ig = ig + lightcolor.getY() * phong.getZ() * Math.max(Math.pow(r.dotproductWith(v),phong.getW()), 0);
					ib = ib + lightcolor.getZ() * phong.getZ() * Math.max(Math.pow(r.dotproductWith(v),phong.getW()), 0);
				}
				
			}
				
			Vec3 intensity = new Vec3(ir,ig,ib);
			Vec3 color = materialColor.multiplyWithScalarVector(intensity);
		    shadedColor = shadedColor.addWith(color);
		}
		return shadedColor;
	}
	
	
	
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

	public static void getImageFromArray(int[] pixels, int width, int height, String link) throws IOException {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        image.setRGB(0,0,width,height,pixels,0,width);
		File outputfile = new File(link);
		ImageIO.write(image, "png", outputfile);
    }
	
	

}
