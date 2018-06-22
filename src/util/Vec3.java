package util;

/**
 * @author Simon
 * Class for a 1x3 vector
 */
public class Vec3 {
	
	double x,y,z;
	
	/**
	 * Constructor of this class
	 * @param x The x value of the vector
	 * @param y The y value of the vector
	 * @param z The z value of the vector
	 */
	public Vec3(double x, double y, double z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * @return A new normalized vector
	 */
	public Vec3 normalize(){
		double len = Math.sqrt(Math.pow(x,2)+Math.pow(y, 2)+Math.pow(z, 2));
		return new Vec3(x/len, y/len, z/len);
		
	}
	
	/**
	 * @param subtractor The vector to substract this vector with
	 * @return A new vector containing the difference
	 */
	public Vec3 subtractedWith(Vec3 subtractor){
		return new Vec3(x-subtractor.getX(),y-subtractor.getY(),z-subtractor.getZ());
	}
	
	/**
	 * @param additor The vector to add this vector with
	 * @return A new vector containing the sum
	 */
	public Vec3 addWith(Vec3 additor){
		return new Vec3(x+additor.getX(),y+additor.getY(),z+additor.getZ());
	}
	
	/**
	 * @param vector A vector containing scalars to multiply the x,y and z of this vector with
	 * @return A new vector containing the product
	 */
	public Vec3 multiplyWithScalarVector(Vec3 vector){
		return new Vec3(x*vector.getX(),y*vector.getY(),z*vector.getZ());
	}
	
	/**
	 * @param scalar A value to multiply each of x, y and z of this vector with
	 * @return A new vector containing the product
	 */
	public Vec3 multiplyWithScalar(double scalar){
		return new Vec3(x*scalar,y*scalar,z*scalar);
	}
	
	/**
	 * @param scalar A scalar to divide each of x, y and z
	 * @return A new vector containing the division
	 */
	public Vec3 dividedByScalar(double scalar) {
		return new Vec3(x/scalar,y/scalar,z/scalar);
	}
	
	/**
	 * @param vector The vector to calculate the crossproduct with
	 * @return A new vector containing the crossproduct
	 */
	public Vec3 crossproductWith(Vec3 vector){
		double a = y*vector.getZ() - z*vector.getY();
		double b = z*vector.getX() - x*vector.getZ();
		double c = x*vector.getY() - y*vector.getX();
		return new Vec3(a,b,c);
	}
	
	/**
	 * @param vector The vector to calculate the dotproduct with
	 * @return The dotproduct of this vector with another
	 */
	public double dotproductWith(Vec3 vector){
		return x*vector.getX() + y*vector.getY() + z*vector.getZ();
	}
	
	/**
	 * @return A new negated vector
	 */
	public Vec3 negate(){
		return new Vec3(-x,-y,-z);
	}
	
	/**
	 * Prints out the vector to the console
	 */
	public void print(){
		System.out.println("x: " + x + " y: " + y + " z: " + z);
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


	/**
	 * @return The z value of the vector
	 */
	public double getZ() {
		return z;
	}


	/**
	 * @param z The z value of the vector
	 */
	public void setZ(double z) {
		this.z = z;
	}

	
	
	
	
	
}
