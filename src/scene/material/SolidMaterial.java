package scene.material;

import util.Vec3;
import util.Vec4;

public class SolidMaterial extends Material{
	
	Vec3 color;

	public SolidMaterial(Vec3 color, Vec4 phong, double reflactance, double transmittance, double refraction) {
		super(phong, reflactance, transmittance, refraction);
		this.color = color;
	}

	public Vec3 getColor() {
		return color;
	}
}
