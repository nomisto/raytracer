package obj;

import util.Vec3;

public class Triangle {
	
	Vec3 vertex1;
	Vec3 vertex2;
	Vec3 vertex3;
	
	public Triangle(Vec3 vertex1, Vec3 vertex2, Vec3 vertex3){
		this.vertex1 = vertex1;
		this.vertex2 = vertex2;
		this.vertex3 = vertex3;
	}

	public Vec3 getVertex1() {
		return vertex1;
	}

	public Vec3 getVertex2() {
		return vertex2;
	}

	public Vec3 getVertex3() {
		return vertex3;
	}

	
}
