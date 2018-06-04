package util;

public class Vec4 {
	double x,y,z,w;
	
	public Vec4(double x, double y, double z, double w){
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}

	public double getW() {
		return w;
	}

	public void print() {
		System.out.println("x: " + x + " y: " + y + " z: " + z + "w: " + w);
	}
}
