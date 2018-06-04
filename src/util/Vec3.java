package util;

public class Vec3 {
	
	double x,y,z;
	
	public Vec3(double x, double y, double z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vec3 normalize(){
		double len = Math.sqrt(Math.pow(x,2)+Math.pow(y, 2)+Math.pow(z, 2));
		return new Vec3(x/len, y/len, z/len);
		
	}
	
	public Vec3 subtractedWith(Vec3 subtractor){
		return new Vec3(x-subtractor.getX(),y-subtractor.getY(),z-subtractor.getZ());
	}
	
	public Vec3 addWith(Vec3 additor){
		return new Vec3(x+additor.getX(),y+additor.getY(),z+additor.getZ());
	}
	
	public Vec3 multiplyWithScalarVector(Vec3 vector){
		return new Vec3(x*vector.getX(),y*vector.getY(),z*vector.getZ());
	}
	
	public Vec3 multiplyWithScalar(double scalar){
		return new Vec3(x*scalar,y*scalar,z*scalar);
	}
	
	public Vec3 crossproductWith(Vec3 vector){
		double a = y*vector.getZ() - z*vector.getY();
		double b = z*vector.getX() - x*vector.getZ();
		double c = x*vector.getY() - y*vector.getX();
		return new Vec3(a,b,c);
	}
	
	public double dotproductWith(Vec3 vector){
		return x*vector.getX() + y*vector.getY() + z*vector.getZ();
	}
	
	public Vec3 negate(){
		return new Vec3(-x,-y,-z);
	}
	
	public void print(){
		System.out.println("x: " + x + " y: " + y + " z: " + z);
	}

	public double getX() {
		return x;
	}


	public void setX(double x) {
		this.x = x;
	}


	public double getY() {
		return y;
	}


	public void setY(double y) {
		this.y = y;
	}


	public double getZ() {
		return z;
	}


	public void setZ(double z) {
		this.z = z;
	}
	
	
	
	
}
