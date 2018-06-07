package scene.material;

import util.Vec3;
import util.Vec4;

/**
 * @author Simon
 * Subclass SolidMaterial of Material
 */
public class SolidMaterial extends Material{
	
	Vec3 color;

	/**
	 * Constructor of this class
	 * @param color Color of the solid material
	 * @param phong Vec4 phong containing the ka, kd, ks and the exponent
	 * @param reflactance Reflactance value
	 * @param transmittance Transmittance value
	 * @param refraction Refraction value
	 */
	public SolidMaterial(Vec3 color, Vec4 phong, double reflactance, double transmittance, double refraction) {
		super(phong, reflactance, transmittance, refraction);
		this.color = color;
	}

	/**
	 * @return The color of the solid material
	 */
	public Vec3 getColor() {
		return color;
	}
}
