package util;

public class Mat4 {
	
	double[][] mat;
	
	public Mat4(){
		mat = new double[4][4];
	}
	
	public void identity(){
		mat = new double[4][4];
		mat[0][0]=1;
		mat[1][1]=1;
		mat[2][2]=1;
		mat[3][3]=1;
	}
	
	public void setRotationAxis(int axis,Vec3 rot){
		mat[0][axis]=rot.getX();
		mat[1][axis]=rot.getY();
		mat[2][axis]=rot.getZ();
	}

	public void setTranslationVector(Vec3 trans){
		mat[0][3]=trans.getX();
		mat[1][3]=trans.getY();
		mat[2][3]=trans.getZ();
	}
	
	public Vec3 multiplyPoint(Vec3 vector){
		double x = mat[0][0] * vector.getX() + mat[0][1] * vector.getY() + mat[0][2] * vector.getZ() + mat[0][3]*1;
		double y = mat[1][0] * vector.getX() + mat[1][1] * vector.getY() + mat[1][2] * vector.getZ() + mat[1][3]*1;
		double z = mat[2][0] * vector.getX() + mat[2][1] * vector.getY() + mat[2][2] * vector.getZ() + mat[2][3]*1;
		return new Vec3(x,y,z);
	}
	
	public Vec3 multiplyVector(Vec3 vector){
		double x = mat[0][0] * vector.getX() + mat[0][1] * vector.getY() + mat[0][2] * vector.getZ();
		double y = mat[1][0] * vector.getX() + mat[1][1] * vector.getY() + mat[1][2] * vector.getZ();
		double z = mat[2][0] * vector.getX() + mat[2][1] * vector.getY() + mat[2][2] * vector.getZ();
		return new Vec3(x,y,z);
	}
}
