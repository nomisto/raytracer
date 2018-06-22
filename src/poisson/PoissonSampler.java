package poisson;

import java.util.ArrayList;
import java.util.Random;

import util.Vec2;

public class PoissonSampler {
	
	ArrayList<Vec2> processingList;
	ArrayList<Vec2> outputList;
	Random r = new Random();
	int k = 30;
	double height,width,minimum;
	
	/**
	 * Construcor
	 * Initializes the pocessing and outputlist and adds a random point to them
	 * @param width The width of the poissondisc in pixel
	 * @param height The height of the poissondisc in pixel
	 * @param minimum The minimum distance between the points
	 */
	public PoissonSampler(double width, double height, double minimum){
		this.height = height;
		this.width = width;
		this.minimum = minimum;
		processingList = new ArrayList<Vec2>();
		outputList = new ArrayList<Vec2>();
		Vec2 first = new Vec2(r.nextDouble()*width,r.nextDouble()*height);
		processingList.add(first);
		outputList.add(first);
		generate();
	}

	/**
	 * Generates all other points with a minimum distance to each other
	 * There are k tries to get a new point from a random point grabbed from the processing list
	 */
	private void generate(){
		while(!processingList.isEmpty()){
			int i = r.nextInt(processingList.size());
			Vec2 point = processingList.remove(i);
			
			for(int j=0; j<k; j++){
				Vec2 newPoint = generateNewPointAround(point,minimum);
				if(distanceCheck(newPoint) && inWindow(newPoint)){
					processingList.add(newPoint);
					outputList.add(newPoint);
				}
			}
		}
		for(Vec2 p : outputList){
			p.setX(p.getX()-(width/2.0));
			p.setY(p.getY()-(height/2.0));
		}
	}
	
	/**
	 * @param point A point of the poissondisc
	 * @return true if the point lies in the window(width, heigth), false otherwise
	 */
	private boolean inWindow(Vec2 point){
		if(point.getX()<0 || point.getY() <0 || point.getX()>width || point.getY()>height){
			return false;
		}
		return true;
	}
	
	/**
	 * @param newPoint Point of the poissondisc
	 * @return True if the minimum distance is fulfilled to each other point, false otherwise
	 */
	private boolean distanceCheck(Vec2 newPoint){
		double distance;
		for(Vec2 point : outputList){
			distance = newPoint.subtractedWith(point).getLength();
			if(distance<minimum){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Generates a new point from a given point, with random angle and random radius (minimum < r < 2* minimum)
	 * @param point A point on the poissondisc
	 * @param minimum The minimum distance
	 * @return A new point
	 */
	private Vec2 generateNewPointAround(Vec2 point, double minimum){
		double random1 = r.nextDouble();
		double random2 = r.nextDouble();
		double radius = minimum*(random1+1);
		double angle = 2*Math.PI*random2;
		double newX = point.getX() + radius*Math.cos(angle);
		double newY = point.getY() + radius*Math.sin(angle);
		return new Vec2(newX, newY);
	}
	
	/**
	 * @return The poissondisc
	 */
	public ArrayList<Vec2> getSampler(){
		return outputList;
	}
}
