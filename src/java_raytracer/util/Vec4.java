package java_raytracer.util;

/**
 * @author Simon
 * Class for a 1x4 vector
 */
public class Vec4 {
	double x,y,z,w;
	
	/**
	 * Constructor of this class
	 * @param x The x value of the vector
	 * @param y The y value of the vector
	 * @param z The z value of the vector
	 * @param w The w value of the vector
	 */
	public Vec4(double x, double y, double z, double w){
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	/**
	 * @return The x value of the vector
	 */
	public double getX() {
		return x;
	}

	/**
	 * @return The y value of the vector
	 */
	public double getY() {
		return y;
	}

	/**
	 * @return The z value of the vector
	 */
	public double getZ() {
		return z;
	}

	/**
	 * @return The w value of the vector
	 */
	public double getW() {
		return w;
	}

	/**
	 * Prints the vector to console
	 */
	public void print() {
		System.out.println("x: " + x + " y: " + y + " z: " + z + "w: " + w);
	}
}
