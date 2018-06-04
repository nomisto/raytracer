package scene.material;

import util.Vec4;

public class TexturedMaterial extends Material{
	
	String texture;

	public TexturedMaterial(String texture, Vec4 phong, double reflactance, double transmittance, double refraction) {
		super(phong, reflactance, transmittance, refraction);
		this.texture = texture;
	}

	public String getTexture() {
		return texture;
	}

	
}
