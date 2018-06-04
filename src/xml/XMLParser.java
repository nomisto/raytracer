package xml;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import scene.Camera;
import scene.*;
import scene.light.*;
import scene.material.*;
import scene.surface.*;
import scene.transformation.*;
import util.Vec3;
import util.Vec4;

public class XMLParser {
	
	public static Scene parseScene(String link){
		try {
	        File inputFile = new File(link);
	        SAXBuilder saxBuilder = new SAXBuilder();
	        Document document = saxBuilder.build(inputFile);
	        
	        
	        Element scene = document.getRootElement();
	        
	        Element backgroundcolorElement = scene.getChild("background_color");
	        Element cameraElement = scene.getChild("camera");
	        Element lightsElement = scene.getChild("lights");
	        Element surfacesElement = scene.getChild("surfaces");
	        
	        
	        //Parse the outputfile
	        String outputfile = scene.getAttributeValue("output_file");
	        
	        //Parse the backgroundcolor
	        double r = Double.parseDouble(backgroundcolorElement.getAttributeValue("r"));
	        double g = Double.parseDouble(backgroundcolorElement.getAttributeValue("g"));
	        double b = Double.parseDouble(backgroundcolorElement.getAttributeValue("b"));
	        Vec3 backgroundcolor = new Vec3(r,g,b);
	        
	        
	        //Parse the values for the camera
	        Element positionElement = cameraElement.getChild("position");
	        Element lookatElement = cameraElement.getChild("lookat");
	        Element upElement = cameraElement.getChild("up");
	        Element horizontalfovElement = cameraElement.getChild("horizontal_fov");
	        Element resolutionElement = cameraElement.getChild("resolution");
	        Element maxbouncesElement = cameraElement.getChild("max_bounces");
	        double positionX = Double.parseDouble(positionElement.getAttributeValue("x"));
	        double positionY = Double.parseDouble(positionElement.getAttributeValue("y"));
	        double positionZ = Double.parseDouble(positionElement.getAttributeValue("z"));
	        Vec3 position = new Vec3(positionX,positionY,positionZ);
	        double lookatX = Double.parseDouble(lookatElement.getAttributeValue("x"));
	        double lookatY = Double.parseDouble(lookatElement.getAttributeValue("y"));
	        double lookatZ = Double.parseDouble(lookatElement.getAttributeValue("z"));
	        Vec3 lookat = new Vec3(lookatX,lookatY,lookatZ);
	        double upX = Double.parseDouble(upElement.getAttributeValue("x"));
	        double upY = Double.parseDouble(upElement.getAttributeValue("y"));
	        double upZ = Double.parseDouble(upElement.getAttributeValue("z"));
	        Vec3 up = new Vec3(upX,upY,upZ);
	        double horizontal_fov = Double.parseDouble(horizontalfovElement.getAttributeValue("angle"));
	        int[] resolution = {Integer.parseInt(resolutionElement.getAttributeValue("horizontal")),Integer.parseInt(resolutionElement.getAttributeValue("vertical"))};
	        int max_bounces = Integer.parseInt(maxbouncesElement.getAttributeValue("n"));
	        Camera camera = new Camera(position,lookat,up,horizontal_fov,resolution,max_bounces);
	        
	        
	        
	        //Parse the values for the lights
	        ArrayList<Light> lights = new ArrayList<Light>();
	        List<Element> lightList = lightsElement.getChildren();
	        for(int i = 0; i < lightList.size(); i++){
	        	Element light = lightList.get(i);
	        	if(light.getName().equals("ambient_light")){
	        		Element colorElement = light.getChild("color");
	        		double rc = Double.parseDouble(colorElement.getAttributeValue("r"));
	        		double gc = Double.parseDouble(colorElement.getAttributeValue("g"));
	        		double bc = Double.parseDouble(colorElement.getAttributeValue("b"));
	        		Vec3 color = new Vec3(rc,gc,bc);
	        		Light amblight = new AmbientLight(color);
	        		lights.add(amblight);
	        	} else if(light.getName().equals("parallel_light")) {
	        		Element colorElement = light.getChild("color");
	        		double rc = Double.parseDouble(colorElement.getAttributeValue("r"));
	        		double gc = Double.parseDouble(colorElement.getAttributeValue("g"));
	        		double bc = Double.parseDouble(colorElement.getAttributeValue("b"));
	        		Vec3 color = new Vec3(rc,gc,bc);
	        		Element directionElement = light.getChild("direction");
	        		double xd = Double.parseDouble(directionElement.getAttributeValue("x"));
	        		double yd = Double.parseDouble(directionElement.getAttributeValue("y"));
	        		double zd = Double.parseDouble(directionElement.getAttributeValue("z"));
	        		Vec3 direction = new Vec3(xd,yd,zd);
	        		Light paralight = new ParallelLight(color,direction);
	        		lights.add(paralight);
	        	} else if(light.getName().equals("point_light")){
	        		Element colorElement = light.getChild("color");
	        		double rc = Double.parseDouble(colorElement.getAttributeValue("r"));
	        		double gc = Double.parseDouble(colorElement.getAttributeValue("g"));
	        		double bc = Double.parseDouble(colorElement.getAttributeValue("b"));
	        		Vec3 color = new Vec3(rc,gc,bc);
	        		Element lightPositionElement = light.getChild("position");
	        		double xp = Double.parseDouble(lightPositionElement.getAttributeValue("x"));
	        		double yp = Double.parseDouble(lightPositionElement.getAttributeValue("y"));
	        		double zp = Double.parseDouble(lightPositionElement.getAttributeValue("z"));
	        		Vec3 lightPosition = new Vec3(xp,yp,zp);
	        		Light paralight = new PointLight(color,lightPosition);
	        		lights.add(paralight);
	        	}
	        }
	        
	        
	        //parse the surfaces
	        ArrayList<Surface> surfaces= new ArrayList<Surface>();
	        List<Element> surfaceList = surfacesElement.getChildren();
	        for(int i = 0; i < surfaceList.size(); i++){
	        	Element surface = surfaceList.get(i);
	        	
	        	Material material = null;
        		if(surface.getChild("material_solid") != null){
        			Element materialElement = surface.getChild("material_solid");
        			Element materialColorElement = materialElement.getChild("color");
        			double rms = Double.parseDouble(materialColorElement.getAttributeValue("r"));
        			double gms = Double.parseDouble(materialColorElement.getAttributeValue("g"));
        			double bms = Double.parseDouble(materialColorElement.getAttributeValue("b"));
        			Vec3 solidmaterialColor = new Vec3(rms,gms,bms);
        			Element phongElement = materialElement.getChild("phong");
        			double ka = Double.parseDouble(phongElement.getAttributeValue("ka"));
        			double kd = Double.parseDouble(phongElement.getAttributeValue("kd"));
        			double ks = Double.parseDouble(phongElement.getAttributeValue("ks"));
        			double exponent = Double.parseDouble(phongElement.getAttributeValue("exponent"));
        			Vec4 phong = new Vec4(ka,kd,ks,exponent);
        			double reflectance = Double.parseDouble(materialElement.getChild("reflectance").getAttributeValue("r"));
        			double transmittance = Double.parseDouble(materialElement.getChild("transmittance").getAttributeValue("t"));
        			double refraction = Double.parseDouble(materialElement.getChild("refraction").getAttributeValue("iof"));
        			material = new SolidMaterial(solidmaterialColor,phong,reflectance,transmittance,refraction);
        		} else if(surface.getChild("material_textured")!=null){
        			Element materialElement = surface.getChild("material_textured");
        			Element textureElement = materialElement.getChild("texture");
        			String texture = textureElement.getAttributeValue("name");
        			Element phongElement = materialElement.getChild("phong");
        			double ka = Double.parseDouble(phongElement.getAttributeValue("ka"));
        			double kd = Double.parseDouble(phongElement.getAttributeValue("kd"));
        			double ks = Double.parseDouble(phongElement.getAttributeValue("ks"));
        			double exponent = Double.parseDouble(phongElement.getAttributeValue("exponent"));
        			Vec4 phong = new Vec4(ka,kd,ks,exponent);
        			double reflectance = Double.parseDouble(materialElement.getChild("reflectance").getAttributeValue("r"));
        			double transmittance = Double.parseDouble(materialElement.getChild("transmittance").getAttributeValue("t"));
        			double refraction = Double.parseDouble(materialElement.getChild("refraction").getAttributeValue("iof"));
        			material = new TexturedMaterial(texture,phong,reflectance,transmittance,refraction);
        		}
        		
        		ArrayList<Transformation> transforms = new ArrayList<Transformation>();
        		if(surface.getChild("transforms") != null){
	        		Element transformationsElement = surface.getChild("transforms");
	        		List<Element> transformationsList = transformationsElement.getChildren();
	        		for(int j = 0; i<transformationsList.size(); i++){
	        			Element transformationElement = transformationsList.get(j);
	        			Transformation transformation = null;
	        			if(transformationElement.getName().equals("translate")){
	        				double tx = Double.parseDouble(transformationElement.getAttributeValue("x"));
	        				double ty = Double.parseDouble(transformationElement.getAttributeValue("y"));
	        				double tz = Double.parseDouble(transformationElement.getAttributeValue("z"));
	        				Vec3 vector = new Vec3(tx,ty,tz);
	        				transformation = new Translation(vector);
	        			} else if(transformationElement.getName().equals("scale")){
	        				double scx = Double.parseDouble(transformationElement.getAttributeValue("x"));
	        				double scy = Double.parseDouble(transformationElement.getAttributeValue("y"));
	        				double scz = Double.parseDouble(transformationElement.getAttributeValue("z"));
	        				Vec3 vector = new Vec3(scx,scy,scz);
	        				transformation = new Scale(vector);
	        			} else if(transformationElement.getName().equals("rotateX")){
	        				double theta = Double.parseDouble(transformationElement.getAttributeValue("theta"));
	        				transformation = new Rotation(theta,1);
	        			} else if(transformationElement.getName().equals("rotateY")){
	        				double theta = Double.parseDouble(transformationElement.getAttributeValue("theta"));
	        				transformation = new Rotation(theta,2);
	        			} else if(transformationElement.getName().equals("rotateZ")){
	        				double theta = Double.parseDouble(transformationElement.getAttributeValue("theta"));
	        				transformation = new Rotation(theta,3);
	        			}
	        			transforms.add(transformation);
	        		}
        		}
	        	
	        	if(surface.getName().equals("sphere")){
	        		double radius = Double.parseDouble(surface.getAttributeValue("radius"));
	        		Element spherePositionElement = surface.getChild("position");
	        		double xs = Double.parseDouble(spherePositionElement.getAttributeValue("x"));
	        		double ys = Double.parseDouble(spherePositionElement.getAttributeValue("y"));
	        		double zs = Double.parseDouble(spherePositionElement.getAttributeValue("z"));
	        		Vec3 spherePosition = new Vec3(xs,ys,zs);
	        		Surface sphere = new Sphere(radius,spherePosition,material,transforms);
	        		surfaces.add(sphere);
	        	} else if(surface.getName().equals("mesh")){
	        		String surfName = surface.getAttributeValue("name");
	        		Surface mesh = new Mesh(surfName,material,transforms);
	        		surfaces.add(mesh);
	        	}
	        }
	        
	        
	        
	        return new Scene(outputfile,backgroundcolor,camera,lights,surfaces);
	     } catch(JDOMException e) {
	        e.printStackTrace();
	     } catch(IOException ioe) {
	        ioe.printStackTrace();
	     }
		return null;
	}
}
