package scene.surface;

import java.util.ArrayList;

import scene.material.Material;
import scene.transformation.Transformation;
import util.Vec3;

public class Sphere extends Surface{
	
	double radius;
	Vec3 position;

	public Sphere(double radius, Vec3 position, Material material, ArrayList<Transformation> transformations) {
		super(material, transformations);
		this.radius = radius;
		this.position = position;
	}

	public double getRadius() {
		return radius;
	}

	public Vec3 getPosition() {
		return position;
	}

}
