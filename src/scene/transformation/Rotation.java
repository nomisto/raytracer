package scene.transformation;

/**
 * @author Simon
 * Not used yet in this implementation
 */
public class Rotation extends Transformation{
	
	double theta;
	int axis;
	
	/**
	 * @param theta The degrees to rotate by
	 * @param axis The axis to rotate by. 0-x, 1-y, 2-z
	 */
	public Rotation(double theta,int axis){
		this.theta = theta;
		this.axis = axis;
	}

}
