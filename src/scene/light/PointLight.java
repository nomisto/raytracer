package scene.light;

import util.Vec3;

public class PointLight extends Light{

	Vec3 position;
	
	public PointLight(Vec3 color, Vec3 position) {
		super(color);
		this.position = position;
	}

	public Vec3 getPosition() {
		return position;
	}

	
}
