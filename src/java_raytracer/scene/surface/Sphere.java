package java_raytracer.scene.surface;

import java.util.ArrayList;

import java_raytracer.scene.material.Material;
import java_raytracer.scene.transformation.Transformation;
import java_raytracer.util.Vec3;

/**
 * @author Simon
 * Subclass of Surface, contains all values of a sphere
 */
public class Sphere extends Surface{
	
	double radius;
	Vec3 position;

	/**
	 * Constructor of this class
	 * @param radius The radius of the sphere
	 * @param position The position of the midpoint of the sphere
	 * @param material The material of the sphere
	 * @param transformations The transformations of the sphere
	 */
	public Sphere(double radius, Vec3 position, Material material, ArrayList<Transformation> transformations) {
		super(material, transformations);
		this.radius = radius;
		this.position = position;
	}

	/**
	 * @return The radius of the sphere
	 */
	public double getRadius() {
		return radius;
	}

	/**
	 * @return The position of the midpoint of the sphere
	 */
	public Vec3 getPosition() {
		return position;
	}

}
