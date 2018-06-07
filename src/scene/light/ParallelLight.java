package scene.light;

import util.Vec3;

/**
 * @author Simon
 * ParallelLight class, extends Light
 */
public class ParallelLight extends Light {
	
	Vec3 direction;

	/**
	 * Constructor of this class
	 * @param color Color of the parallel light
	 * @param direction Direction of the parallel light
	 */
	public ParallelLight(Vec3 color,Vec3 direction) {
		super(color);
		this.direction = direction;
	}

	/**
	 * @return The direction of the parallel light
	 */
	public Vec3 getDirection() {
		return direction;
	}
}
