package scene.light;

import util.Vec3;

/**
 * @author Simon
 * PointLight class, extends Light
 */
public class PointLight extends Light{

	Vec3 position;
	
	/**
	 * Constructor of this class
	 * @param color The color of the point light
	 * @param position The position of the point light
	 */
	public PointLight(Vec3 color, Vec3 position) {
		super(color);
		this.position = position;
	}

	/**
	 * @return The position of the point light
	 */
	public Vec3 getPosition() {
		return position;
	}

	
}
