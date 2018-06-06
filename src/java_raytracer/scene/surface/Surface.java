package java_raytracer.scene.surface;

import java.util.ArrayList;

import java_raytracer.scene.material.Material;
import java_raytracer.scene.transformation.Transformation;

/**
 * @author Simon
 * Superclass of Sphere and Mesh
 */
public abstract class Surface {
	Material material;
	ArrayList<Transformation> transformations;
	
	/**
	 * Constructor of this class
	 * @param material The material of the surface
	 * @param transformations The transformations of the surface
	 */
	public Surface(Material material, ArrayList<Transformation> transformations){
		this.material = material;
		this.transformations = transformations;
	}

	/**
	 * @return The material of the surface
	 */
	public Material getMaterial() {
		return material;
	}

	/**
	 * @return The transformations of the surface
	 */
	public ArrayList<Transformation> getTransformations() {
		return transformations;
	}

	
}
