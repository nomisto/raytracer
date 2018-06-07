package scene.transformation;

import util.Vec3;

/**
 * @author Simon
 * Not used yet in this implementation
 */
public class Scale extends Transformation{
	
	Vec3 vector;
	
	/**
	 * Constructor of this class
	 * @param vector The vector to scale by
	 */
	public Scale(Vec3 vector){
		this.vector = vector;
	}
}
