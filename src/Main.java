


import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import global.GLOBAL;
import renderer.Renderer;
import scene.Scene;
import xml.XMLParser;

public class Main {


	public static void main(String[] args) throws IOException {
		
		if(args.length != 0){
			//saves the path to a global variable
			File f = new File(args[0]);
			Path p = f.toPath().getParent();
			GLOBAL.path = p;
			
			//parse the xml file
			Scene s = XMLParser.parseScene(args[0]);
			System.out.println("Parsing finished, started Rendering");
			//render and saves the scene
			Renderer r = null;
			if(args.length <2){
				r = new Renderer(s);
			} else if(args[1].equals("ss") && args.length<6){
				double poissonWidth = Double.parseDouble(args[2]);
				double poissonHeight = Double.parseDouble(args[3]);
				double minimum = Double.parseDouble(args[4]);
				r = new Renderer(s,poissonWidth,poissonHeight,minimum);
			}
			r.render();
			System.out.println("Rendering finished. Output can be found in the folder: " + p.toString());
		}
	}
}
