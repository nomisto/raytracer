package java_raytracer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import scene.Scene;
import xml.XMLParser;

public class Main {

	public static void main(String[] args) throws IOException {
		
		File f = new File("C:/Users/Simon/OneDrive/Informatik/CG - Foundations of Computer Graphics/Repositories/scenes/example2.xml");
		Path p = f.toPath().getParent();
		GLOBAL.path = p;
		Scene s = XMLParser.parseScene(GLOBAL.path.toString() + "/example2.xml");
		Renderer r = new Renderer(s);
		r.render();
		
		
	}
}
