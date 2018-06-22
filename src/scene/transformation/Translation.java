package scene.transformation;

import util.Vec3;

/**
 * @author Simon
 * Not used yet in this implementation
 */
public class Translation extends Transformation{
	
	Vec3 vector;
	
	/**
	 * @param vector The vector to translyte by
	 */
	public Translation(Vec3 vector){
		this.vector = vector;
	}

	public Vec3 getVector() {
		return vector;
	}

	
}
