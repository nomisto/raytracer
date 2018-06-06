package java_raytracer;


import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import java_raytracer.scene.Scene;
import java_raytracer.xml.XMLParser;

public class Main {


	public static void main(String[] args) throws IOException {
		
		//saves the path to a global variable
		File f = new File(args[0]);
		Path p = f.toPath().getParent();
		GLOBAL.path = p;
		
		//parse the xml file
		Scene s = XMLParser.parseScene(args[0]);
		System.out.println("Parsing finished, started Rendering");
		//render and saves the scene
		Renderer r = new Renderer(s);
		r.render();
		System.out.println("Rendering finished. Output can be found in the folder: " + p.toString());
		
	}
}
