package java_raytracer.scene;

import java.util.ArrayList;

import java_raytracer.scene.light.Light;
import java_raytracer.scene.surface.Surface;
import java_raytracer.util.Vec3;

/**
 * @author Simon
 * The scene class contains all the elements of a scene (camera, lights, surfaces, ...)
 */
public class Scene {
	
	String outputfile;
	Vec3 background_color;
	Camera camera;
	ArrayList<Light> lights;
	ArrayList<Surface> surfaces;
	
	/**
	 * Constructor of this class
	 * @param outputfile The name of the outputimage
	 * @param background_color The background color
	 * @param camera The camera
	 * @param lights The lights
	 * @param surfaces The surfaces
	 */
	public Scene(String outputfile, Vec3 background_color, Camera camera, ArrayList<Light> lights, ArrayList<Surface> surfaces){
		this.outputfile = outputfile;
		this.background_color = background_color;
		this.camera = camera;
		this.lights = lights;
		this.surfaces = surfaces;
	}

	/**
	 * @return The name of the outputimage
	 */
	public String getOutputfile() {
		return outputfile;
	}

	/**
	 * @return The backgroundcolor
	 */
	public Vec3 getBackground_color() {
		return background_color;
	}

	/**
	 * @return The camera
	 */
	public Camera getCamera() {
		return camera;
	}

	/**
	 * @return The lights
	 */
	public ArrayList<Light> getLights() {
		return lights;
	}

	/**
	 * @return The surfaces
	 */
	public ArrayList<Surface> getSurfaces() {
		return surfaces;
	}
	
	

}
