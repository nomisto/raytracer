package scene.surface;

import java.io.IOException;
import java.util.ArrayList;

import obj.OBJParser;
import scene.material.Material;
import scene.transformation.Transformation;

public class Mesh extends Surface{
	
	String name;
	OBJParser obj;

	public Mesh(String name,Material material, ArrayList<Transformation> transformations) {
		super(material, transformations);
		this.name = name;
		try {
			obj = new OBJParser(name);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public OBJParser getObj(){
		return obj;
	}
}
