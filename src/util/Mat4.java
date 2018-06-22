package util;

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
	
	public Mat4(double[][] mat) {
		this.mat = mat;
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
		double w = mat[3][0] * point.getX() + mat[3][1] * point.getY() + mat[3][2] * point.getZ() + mat[3][3]*1;
		return new Vec3(x/w,y/w,z/w);
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
		double w = mat[3][0] * vector.getX() + mat[3][1] * vector.getY() + mat[3][2] * vector.getZ() + mat[3][3]*1;
		return new Vec3(x/w,y/w,z/w);
	}
	
	public Vec3 multiplyNormal(Vec3 normal){
		double x = mat[0][0] * normal.getX() + mat[0][1] * normal.getY() + mat[0][2] * normal.getZ() + mat[0][3]*1;
		double y = mat[1][0] * normal.getX() + mat[1][1] * normal.getY() + mat[1][2] * normal.getZ() + mat[1][3]*1;
		double z = mat[2][0] * normal.getX() + mat[2][1] * normal.getY() + mat[2][2] * normal.getZ() + mat[2][3]*1;
		return new Vec3(x,y,z);
	}
	
	public Mat4 translate(double x, double y, double z) {
		Mat4 t = new Mat4();
		t.identity();
		t.set(0, 3, x);
		t.set(1, 3, y);
		t.set(2, 3, z);
		return multiplyWith(t);
	}
	
	public Mat4 rotateX(double theta) {
		theta = degToRad(theta);
		Mat4 r = new Mat4();
		r.identity();
		r.set(1,1, Math.cos(theta));
		r.set(2, 2, Math.cos(theta));
		r.set(1, 2, -Math.sin(theta));
		r.set(2, 1, Math.sin(theta));
		return multiplyWith(r);
	}
	
	public Mat4 rotateY(double theta) {
		theta = degToRad(theta);
		Mat4 r = new Mat4();
		r.identity();
		r.set(0, 0, Math.cos(theta));
		r.set(2, 2, Math.cos(theta));
		r.set(0, 2, Math.sin(theta));
		r.set(2, 0, -Math.sin(theta));
		return multiplyWith(r);
	}
	
	public Mat4 rotateZ(double theta) {
		theta = degToRad(theta);
		Mat4 r = new Mat4();
		r.identity();
		r.set(0,0,Math.cos(theta));
		r.set(1, 1, Math.cos(theta));
		r.set(0, 1, -Math.sin(theta));
		r.set(1, 0, Math.sin(theta));
		return multiplyWith(r);
	}
	
	public Mat4 scale(double x, double y, double z) {
		Mat4 s = new Mat4();
		s.identity();
		s.set(0,0,x);
		s.set(1,1,y);
		s.set(2, 2, z);
		return multiplyWith(s);
	}
	
	public Mat4 multiplyWith(Mat4 m) {
		double[][] res = new double[4][4];
		int i, j, k;
        for (i = 0; i < 4; i++)
        {
            for (j = 0; j < 4; j++)
            {
                res[i][j] = 0;
                for (k = 0; k < 4; k++)
                    res[i][j] += mat[i][k] 
                                * m.get(k,j);
            }
        }
        return new Mat4(res);
	}
	
	public Mat4 transpose() {
		double[][] res = new double[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                res[j][i] = mat[i][j];
            }
        }
		return new Mat4(res);
	}
	
	public Mat4 inverse(Mat4 inv) {
		Vec3 v = new Vec3(mat[0][3],mat[1][3],mat[2][3]);
		v = inv.multiplyVector(v);
		inv.set(0, 3, -v.getX());
		inv.set(1, 3, -v.getY());
		inv.set(1, 3, -v.getZ());
		return inv;
	}
	
	public double degToRad(double deg) {
		return deg * Math.PI/180.0;
	}
	
	public void set(int x, int y, double value) {
		mat[x][y]=value;
	}
	
	public double get(int x, int y) {
		return mat[x][y];
	}
	
	public double[][] getAll(){
		return mat;
	}
	
	public void print() {
		for(int i =0; i<4; i++) {
			for(int j=0; j<4; j++) {
				System.out.print(mat[i][j]+" ");
			}
			System.out.print("\n");
		}
	}
}
