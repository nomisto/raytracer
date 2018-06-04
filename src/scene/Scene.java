package scene;

import java.util.ArrayList;

import scene.light.Light;
import scene.surface.Surface;
import util.Vec3;

public class Scene {
	
	String outputfile;
	Vec3 background_color;
	Camera camera;
	ArrayList<Light> lights;
	ArrayList<Surface> surfaces;
	
	public Scene(String outputfile, Vec3 background_color, Camera camera, ArrayList<Light> lights, ArrayList<Surface> surfaces){
		this.outputfile = outputfile;
		this.background_color = background_color;
		this.camera = camera;
		this.lights = lights;
		this.surfaces = surfaces;
	}
	
	public void print(){
		System.out.println("outputfile: " + outputfile);
		System.out.println("Color: " + background_color.getX() + " " + background_color.getY() + " " + background_color.getZ());
		System.out.println("\nCamera");
		System.out.println("Position: " + camera.getPosition().getX() + " " + camera.getPosition().getY() + " " + camera.getPosition().getZ());
		System.out.println("Lookat: " + camera.getLookat().getX() + " " + camera.getLookat().getY() + " " + camera.getLookat().getZ());
	}

	public String getOutputfile() {
		return outputfile;
	}

	public Vec3 getBackground_color() {
		return background_color;
	}

	public Camera getCamera() {
		return camera;
	}

	public ArrayList<Light> getLights() {
		return lights;
	}

	public ArrayList<Surface> getSurfaces() {
		return surfaces;
	}
	
	

}
