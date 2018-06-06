package java_raytracer.util;

/**
 * @author Simon
 * Mat3 is the 3by3 Matrix class.
 */
public class Mat3 {
	
	double[][] mat;
	
	/**
	 * Consturctor of this class.
	 * Initializes a 3by3 matrix with zeros.
	 */
	public Mat3(){
		mat = new double[3][3];
	}
	
	/**
	 * Sets a freshly constructed Mat3 to identity matrix
	 */
	public void identity(){
		mat[0][0]=1;
		mat[1][1]=1;
		mat[2][2]=1;
	}
	
	/**
	 * Initializes the 3by3 matrix (a topleft, c bottomleft, g topright, i bottomright);
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @param e
	 * @param f
	 * @param g
	 * @param h
	 * @param i
	 */
	public void fromValues(double a, double b, double c, double d, double e, double f, double g, double h, double i){
		mat[0][0]=a;
		mat[1][0]=b;
		mat[2][0]=c;
		mat[0][1]=d;
		mat[1][1]=e;
		mat[2][1]=f;
		mat[0][2]=g;
		mat[1][2]=h;
		mat[2][2]=i;
	}
	
	/**
	 * @return The determinant of the matrix
	 */
	public double getDeterminant(){
		double a = mat[0][0]*(mat[1][1]*mat[2][2]-mat[1][2]*mat[2][1]);
		double b = mat[0][1]*(mat[1][0]*mat[2][2]-mat[1][2]*mat[2][0]);
		double c = mat[0][2]*(mat[1][0]*mat[2][1]-mat[1][1]*mat[2][0]);
		return a-b+c;
	}

}
