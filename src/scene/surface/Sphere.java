package scene.surface;

import scene.material.Material;
import util.Mat4;
import util.Vec3;

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
	public Sphere(double radius, Vec3 position, Material material, Mat4 transformationMatrix) {
		super(material, transformationMatrix);
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
