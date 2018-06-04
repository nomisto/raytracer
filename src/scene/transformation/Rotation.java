package scene.transformation;

public class Rotation extends Transformation{
	
	double theta;
	int axis;
	
	public Rotation(double theta,int axis){
		this.theta = theta;
		this.axis = axis;
	}

}
