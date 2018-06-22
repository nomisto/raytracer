package scene.surface;

import scene.material.Material;
import util.Mat4;

/**
 * @author Simon
 * Superclass of Sphere and Mesh
 */
public abstract class Surface {
	Material material;
	Mat4 transformationMatrix;
	
	/**
	 * Constructor of this class
	 * @param material The material of the surface
	 * @param transformations The transformations of the surface
	 */
	public Surface(Material material, Mat4 transformationMatrix){
		this.material = material;
		this.transformationMatrix = transformationMatrix;
	}

	/**
	 * @return The material of the surface
	 */
	public Material getMaterial() {
		return material;
	}

	public Mat4 getTransformationMatrix() {
		return transformationMatrix;
	}

	
}
