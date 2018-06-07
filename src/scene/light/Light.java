package scene.light;

import util.Vec3;

/**
 * @author Simon
 * Superclass. Is extended by AmbientLight, ParallelLight and PointLight
 */
public abstract class Light {
	
	Vec3 color;
	
	/**
	 * Constructor of this class
	 * @param color Color of the light
	 */
	public Light(Vec3 color){
		this.color = color;
	}

	/**
	 * @return The color of the light
	 */
	public Vec3 getColor() {
		return color;
	}
}
