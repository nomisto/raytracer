package scene.material;

import util.Vec4;

public abstract class Material {
	
	Vec4 phong;
	double reflactance, transmittance,refraction;
	
	public Material(Vec4 phong, double reflactance, double transmittance, double refraction){
		this.phong = phong;
		this.reflactance = reflactance;
		this.transmittance = transmittance;
		this.refraction = refraction;
	}

	public Vec4 getPhong() {
		return phong;
	}

	public double getReflactance() {
		return reflactance;
	}

	public double getTransmittance() {
		return transmittance;
	}

	public double getRefraction() {
		return refraction;
	}
	
	
}
