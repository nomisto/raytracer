package scene.light;

import util.Vec3;

public class ParallelLight extends Light {
	
	Vec3 direction;

	public ParallelLight(Vec3 color,Vec3 direction) {
		super(color);
		this.direction = direction;
	}

	public Vec3 getDirection() {
		return direction;
	}
}
