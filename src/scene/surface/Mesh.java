package scene.surface;

import java.io.IOException;
import java.util.ArrayList;

import obj.OBJParser;
import scene.material.Material;
import scene.transformation.Transformation;
import util.Vec3;

/**
 * @author Simon
 * Subclass of Surface. Contains all elements of a Mesh.
 */
public class Mesh extends Surface{
	
	String name;
	OBJParser obj;
	
	Vec3 nearest1;
	Vec3 nearest2;
	Vec3 nearest3;
	double nearestbeta;
	double nearestgamma;

	/**
	 * Constructor of this class, calls the OBJParser to parse the obj file
	 * @param name The name of the obj file
	 * @param material The material of this mesh
	 * @param transformations The transformations of this mesh
	 */
	public Mesh(String name,Material material, ArrayList<Transformation> transformations) {
		super(material, transformations);
		this.name = name;
		try {
			obj = new OBJParser(name);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @return The OBJParser Object
	 */
	public OBJParser getObj(){
		return obj;
	}

	/**
	 * Used for shading after an successful intersetion test
	 * @return The first nearest vertex
	 */
	public Vec3 getNearest1() {
		return nearest1;
	}

	/**
	 * Used for shading after an successful intersetion test
	 * @param nearest1 The second nearest vertex
	 */
	public void setNearest1(Vec3 nearest1) {
		this.nearest1 = nearest1;
	}

	/**
	 * Used for shading after an successful intersetion test
	 * @return The second nearest vertex
	 */
	public Vec3 getNearest2() {
		return nearest2;
	}

	/**
	 * Used for shading after an successful intersetion test
	 * @param nearest2 The third nearest vertex
	 */
	public void setNearest2(Vec3 nearest2) {
		this.nearest2 = nearest2;
	}

	/**
	 * Used for shading after an successful intersetion test
	 * @return The first nearest vertex
	 */
	public Vec3 getNearest3() {
		return nearest3;
	}

	/**
	 * Used for shading after an successful intersetion test
	 * @param nearest3 The third nearest vertex
	 */
	public void setNearest3(Vec3 nearest3) {
		this.nearest3 = nearest3;
	}

	/**
	 * Used for shading after an successful intersetion test
	 * @return The beta value of the intersection test
	 */
	public double getNearestbeta() {
		return nearestbeta;
	}

	/**
	 * Used for shading after an successful intersetion test
	 * @param nearestbeta The beta value of the intersection test
	 */
	public void setNearestbeta(double nearestbeta) {
		this.nearestbeta = nearestbeta;
	}

	/**
	 * Used for shading after an successful intersetion test
	 * @return The gamma value of the intersection test
	 */
	public double getNearestgamma() {
		return nearestgamma;
	}

	/**
	 * Used for shading after an successful intersetion test
	 * @param nearestgamma The gamma value of the intersection test
	 */
	public void setNearestgamma(double nearestgamma) {
		this.nearestgamma = nearestgamma;
	}

	
	
}
