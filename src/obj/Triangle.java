package obj;

import util.Vec3;

/**
 * @author Simon
 * Triangle class for a face of a mesh
 */
public class Triangle {
	
	Vec3 vertex1;
	Vec3 vertex2;
	Vec3 vertex3;
	
	/**
	 * Constructor which saves the three vertices
	 * @param vertex1
	 * @param vertex2
	 * @param vertex3
	 */
	public Triangle(Vec3 vertex1, Vec3 vertex2, Vec3 vertex3){
		this.vertex1 = vertex1;
		this.vertex2 = vertex2;
		this.vertex3 = vertex3;
	}

	/**
	 * @return Vertex1 of the triangle in Vec3
	 */
	public Vec3 getVertex1() {
		return vertex1;
	}

	/**
	 * @return Vertex2 of the triangle in Vec3
	 */
	public Vec3 getVertex2() {
		return vertex2;
	}

	/**
	 * @return Vertex3 of the triangle in Vec3
	 */
	public Vec3 getVertex3() {
		return vertex3;
	}

	
}
