package java_raytracer.scene.light;

import java_raytracer.util.Vec3;

/**
 * @author Simon
 * Class AmbientLight for ambient light in the scene
 * Subclass of Light
 */
public class AmbientLight extends Light{

	/**
	 * Constructor of this class
	 * @param color The color of the ambient light
	 */
	public AmbientLight(Vec3 color) {
		super(color);
	}

}
