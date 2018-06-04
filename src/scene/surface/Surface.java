package scene.surface;

import java.util.ArrayList;

import scene.material.Material;
import scene.transformation.Transformation;

public abstract class Surface {
	Material material;
	ArrayList<Transformation> transformations;
	
	public Surface(Material material, ArrayList<Transformation> transformations){
		this.material = material;
		this.transformations = transformations;
	}

	public Material getMaterial() {
		return material;
	}

	public ArrayList<Transformation> getTransformations() {
		return transformations;
	}

	
}
