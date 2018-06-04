package scene.light;

import util.Vec3;

public abstract class Light {
	
	Vec3 color;
	
	public Light(Vec3 color){
		this.color = color;
	}

	public Vec3 getColor() {
		return color;
	}
}
