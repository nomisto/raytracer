package java_raytracer.util;

/**
 * @author Simon
 * Mat4 is the 4by4 Matrix class.
 */
public class Mat4 {
	
	double[][] mat;
	
	/**
	 * Consturctor of this class.
	 * Initializes a 3by3 matrix with zeros.
	 */
	public Mat4(){
		mat = new double[4][4];
	}
	
	/**
	 * Sets a freshly constructed Mat3 to identity matrix
	 */
	public void identity(){
		mat = new double[4][4];
		mat[0][0]=1;
		mat[1][1]=1;
		mat[2][2]=1;
		mat[3][3]=1;
	}
	
	/**
	 * Sets the Rotation Axis
	 * @param axis Axis to rotate into, 0-x,1-y,2-z
	 * @param rot Vector of the rotation
	 */
	public void setRotationAxis(int axis,Vec3 rot){
		mat[0][axis]=rot.getX();
		mat[1][axis]=rot.getY();
		mat[2][axis]=rot.getZ();
	}

	/**
	 * Sets the translation vector in the matrix
	 * @param trans Vector to translate by
	 */
	public void setTranslationVector(Vec3 trans){
		mat[0][3]=trans.getX();
		mat[1][3]=trans.getY();
		mat[2][3]=trans.getZ();
	}
	
	/**
	 * Multiplies a Point with a Mat4 (No translation)
	 * @param point The point to multiply by
	 * @return Result of the multiplication (Vec3)
	 */
	public Vec3 multiplyPoint(Vec3 point){
		double x = mat[0][0] * point.getX() + mat[0][1] * point.getY() + mat[0][2] * point.getZ() + mat[0][3]*1;
		double y = mat[1][0] * point.getX() + mat[1][1] * point.getY() + mat[1][2] * point.getZ() + mat[1][3]*1;
		double z = mat[2][0] * point.getX() + mat[2][1] * point.getY() + mat[2][2] * point.getZ() + mat[2][3]*1;
		return new Vec3(x,y,z);
	}
	
	/**
	 * Multiplies a Vector with a Mat4
	 * @param vector The vector to multiply by
	 * @return Result of the multiplication (Vec3)
	 */
	public Vec3 multiplyVector(Vec3 vector){
		double x = mat[0][0] * vector.getX() + mat[0][1] * vector.getY() + mat[0][2] * vector.getZ();
		double y = mat[1][0] * vector.getX() + mat[1][1] * vector.getY() + mat[1][2] * vector.getZ();
		double z = mat[2][0] * vector.getX() + mat[2][1] * vector.getY() + mat[2][2] * vector.getZ();
		return new Vec3(x,y,z);
	}
}
