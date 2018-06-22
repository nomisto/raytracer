package scene;

import renderer.Ray;
import util.Mat4;
import util.Vec3;

public class Camera {
	
	Vec3 position;
	Vec3 lookat;
	Vec3 up;
	double horizontal_fov;
	int[] resolution;
	int max_bounces;
	
	Vec3 camX,camY,camZ;
	Mat4 m;
	double whalf,hhalf;
	double wres,hres;
	
	public Camera(Vec3 position, Vec3 lookat, Vec3 up, double horizontal_fov, int[] resolution,int max_bounces){
		this.position = position;
		this.lookat = lookat;
		this.up = up;
		this.horizontal_fov = horizontal_fov;
		this.resolution = resolution;
		this.max_bounces = max_bounces;
		camZ = this.position.subtractedWith(lookat);
		camZ = camZ.normalize();
		camX = this.up.crossproductWith(camZ);
		camX = camX.normalize();
		camY = camZ.crossproductWith(camX);
		camY = camY.normalize();
		m = new Mat4();
		m.identity();
		m.setRotationAxis(0, camX);
		m.setRotationAxis(1, camY);
		m.setRotationAxis(2, camZ);
		m.setTranslationVector(this.position);
		double fovRAD = this.horizontal_fov*Math.PI/180;
		double w = Math.tan(fovRAD)*2;
		double h = Math.tan(fovRAD*(this.resolution[1]/this.resolution[0]))*2;
		whalf = w/2;
		hhalf = h/2;
		wres = w/this.resolution[0];
		hres = h/this.resolution[1];
	}
	
	public Ray constructEyeRay(double x,double y){
		double xPixel = wres * (x + 0.5) - whalf;
		double yPixel = -hres * (y + 0.5) + hhalf;
		double zPixel = -1;
		Vec3 pixel = new Vec3(xPixel, yPixel, zPixel);
		Vec3 origin = new Vec3(0,0,0);
		Vec3 direction = pixel.subtractedWith(origin);
		direction = direction.normalize();
		Vec3 orgWorld = m.multiplyPoint(origin);
		Vec3 dirWorld = m.multiplyVector(direction);
		dirWorld = dirWorld.normalize();
		return new Ray(orgWorld, dirWorld);
	}

	public Vec3 getPosition() {
		return position;
	}

	public Vec3 getLookat() {
		return lookat;
	}

	public Vec3 getUp() {
		return up;
	}

	public double getHorizontal_fov() {
		return horizontal_fov;
	}

	public int[] getResolution() {
		return resolution;
	}

	public int getMax_bounces() {
		return max_bounces;
	}
}
