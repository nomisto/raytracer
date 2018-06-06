package java_raytracer.scene.material;

import java_raytracer.util.Vec4;

/**
 * @author Simon
 * Subclass TexturedMaterial of Material
 */
public class TexturedMaterial extends Material{
	
	String texture;

	/**
	 *  Constructor of this class
	 * @param texture The name of the texture file
	 * @param phong Vec4 phong containing the ka, kd, ks and the exponent
	 * @param reflactance Reflactance value
	 * @param transmittance Transmittance value
	 * @param refraction Refraction value
	 */
	public TexturedMaterial(String texture, Vec4 phong, double reflactance, double transmittance, double refraction) {
		super(phong, reflactance, transmittance, refraction);
		this.texture = texture;
	}

	/**
	 * @return The name of the texture file
	 */
	public String getTexture() {
		return texture;
	}

	
}
