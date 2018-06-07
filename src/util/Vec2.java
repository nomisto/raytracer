package util;

/**
 * @author Simon
 * Class for a 1x2 vector
 */
public class Vec2 {
	
	double x;
	double y;
	
	/**
	 * Constructor of this class
	 * @param x The x value of the vector
	 * @param y The y value of the vector
	 */
	public Vec2(double x, double y){
		this.x = x;
		this.y = y;
	}

	/**
	 * @return The x value of the vector
	 */
	public double getX() {
		return x;
	}

	/**
	 * @param x The x value of the vector
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * @return The y value of the vector
	 */
	public double getY() {
		return y;
	}

	/**
	 * @param y The y value of the vector
	 */
	public void setY(double y) {
		this.y = y;
	}

	
}
