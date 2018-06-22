package scene.surface;

import java.io.IOException;
import obj.OBJParser;
import scene.material.Material;
import util.Mat4;
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
	public Mesh(String name,Material material, Mat4 transformationMatrix) {
		super(material, transformationMatrix);
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

	public String getName() {
		return name;
	}
	
	
}
