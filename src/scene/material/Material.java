package scene.material;

import util.Vec4;

/**
 * @author Simon
 * Superclass. Extended by SolidMaterial and TexturedMaterial
 */
public abstract class Material {
	
	Vec4 phong;
	double reflactance, transmittance,refraction;
	
	/**
	 * Constructor of this class
	 * @param phong Vec4 phong containing the ka, kd, ks and the exponent
	 * @param reflactance Reflactance value
	 * @param transmittance Transmittance value
	 * @param refraction Refraction value
	 */
	public Material(Vec4 phong, double reflactance, double transmittance, double refraction){
		this.phong = phong;
		this.reflactance = reflactance;
		this.transmittance = transmittance;
		this.refraction = refraction;
	}

	/**
	 * @return Vec4 phong containing the ka, kd, ks and the exponent
	 */
	public Vec4 getPhong() {
		return phong;
	}

	/**
	 * @return Reflactance value
	 */
	public double getReflactance() {
		return reflactance;
	}

	/**
	 * @return Transmittance value
	 */
	public double getTransmittance() {
		return transmittance;
	}

	/**
	 * @return Refraction value
	 */
	public double getRefraction() {
		return refraction;
	}
	
	
}
