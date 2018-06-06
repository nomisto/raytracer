package java_raytracer.scene;

import java_raytracer.util.Mat4;
import java_raytracer.util.Vec3;

/**
 * @author Simon
 * The Camera class contains all elements regarding the camera of the scene. Used for rayconstruction.
 */
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
	
	/**
	 * Constructor of this class
	 * @param position The position of the camera
	 * @param lookat The vector look at
	 * @param up The vector defining where up is
	 * @param horizontal_fov The angle of the horizontal fov in deg
	 * @param resolution The resolution of the image
	 * @param max_bounces Maximal bounces
	 */
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
	
	/**
	 * Constructs an Eye Ray of a given x and y in image space
	 * @param x x coordinate in image space
	 * @param y y coordinate in image space
	 * @return Array of Vec3 containing the origin and the direction of the camera (in world coordinates)
	 */
	public Vec3[] constructEyeRay(int x,int y){
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
		Vec3[] res = {orgWorld, dirWorld};
		return res;
	}

	/**
	 * @return The position of the camera
	 */
	public Vec3 getPosition() {
		return position;
	}

	/**
	 * @return The vector of the look at
	 */
	public Vec3 getLookat() {
		return lookat;
	}

	/**
	 * @return The vecor defining where up is
	 */
	public Vec3 getUp() {
		return up;
	}

	/**
	 * @return The angle of the horizontal fov in deg
	 */
	public double getHorizontal_fov() {
		return horizontal_fov;
	}

	/**
	 * @return Array containing the length of height and width
	 */
	public int[] getResolution() {
		return resolution;
	}

	/**
	 * @return Maximal bounces
	 */
	public int getMax_bounces() {
		return max_bounces;
	}
}
