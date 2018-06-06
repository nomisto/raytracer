package java_raytracer.obj;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import java_raytracer.GLOBAL;
import java_raytracer.util.Vec2;
import java_raytracer.util.Vec3;

/**
 * @author Simon
 * Class OBJParser for parsing the obj file if one exists
 */
public class OBJParser {

	ArrayList<Vec3> vertices = new ArrayList<Vec3>();
	ArrayList<Vec3> normals = new ArrayList<Vec3>();
	ArrayList<Vec2> texcoords = new ArrayList<Vec2>();
	ArrayList<Triangle> triangles = new ArrayList<Triangle>();
	
	/**
	 * Constructor for the Class, initializes the parsing process
	 * only parses faces in format of v/vt/vn
	 * @param name Name of the OBJ file
	 * @throws IOException
	 */
	public OBJParser(String name) throws IOException{
		vertices = new ArrayList<Vec3>();
		normals = new ArrayList<Vec3>();
		texcoords = new ArrayList<Vec2>();
		triangles = new ArrayList<Triangle>();
		
		BufferedReader br = new BufferedReader(new FileReader(GLOBAL.path.toString() + "/" + name));
		String line = br.readLine();
		
		while(line != null){
			char first = line.charAt(0);
			char second = line.charAt(1);
			if(first == 'v' && second == ' '){
				Vec3 vert = parseVector(2,line);
				vertices.add(vert);
			} else if(first == 'v' && second == 't') {
				Vec2 texc = parseTexcoords(line);
				texcoords.add(texc);
			} else if(first =='v' && second == 'n'){
				Vec3 norm = parseVector(3,line);
				normals.add(norm);
			} else if(first == 'f'){
				Triangle tri = parseTriangle(line);
				triangles.add(tri);
			}
			line = br.readLine();
		}
		br.close();
	}


	/**
	 * parses the vectors vertices and normals
	 * @param s Start point in the line (different for v and for vn)
	 * @param line The current line
	 * @return A Vec3 containing the values parsed
	 */
	public static Vec3 parseVector(int s,String line){
		String x = "";
		String y = "";
		String z = "";
	
		int index = s;
		while(line.charAt(index)!=' '){
			x = x + line.charAt(index);
			index++;
		}
		index++;
		while(line.charAt(index)!=' '){
			y = y + line.charAt(index);
			index++;
		}
		index++;
		while(index<line.length()){
			z = z + line.charAt(index);
			index++;
		}
		return new Vec3(Double.parseDouble(x),Double.parseDouble(y),Double.parseDouble(z));
	}
	
	
	
	
	/**
	 * Parses a Vec2 of the texcorrds
	 * @param line The current line
	 * @return Vec2 with the values parsed from the line
	 */
	public static Vec2 parseTexcoords(String line){
		String x = "";
		String y = "";
		
		int index = 3;
		while(line.charAt(index)!=' '){
			x = x + line.charAt(index);
			index++;
		}
		index++;
		while(index<line.length()){
			y = y + line.charAt(index);
			index++;
		}
		return new Vec2(Double.parseDouble(x),Double.parseDouble(y));
	}
	
	
	
	/**
	 * Parses a face (triangle), only in the format v/vt/vn
	 * @param line The current Line
	 * @return A triangle object containing three Vec3 each containing the indices of the v, vt and vn
	 */
	public static Triangle parseTriangle(String line){
		int index = 2;
		String v1 = "";
		String t1 = "";
		String n1 = "";
		String v2 = "";
		String t2 = "";
		String n2 = "";
		String v3 = "";
		String t3 = "";
		String n3 = "";
		
		while(line.charAt(index)!='/'){
			v1 = v1 + line.charAt(index);
			index++;
		}
		index++;
		while(line.charAt(index)!='/'){
			t1 = t1 + line.charAt(index);
			index++;
		}
		index++;
		while(line.charAt(index)!=' '){
			n1 = n1 + line.charAt(index);
			index++;
		}
		index++;
		while(line.charAt(index)!='/'){
			v2 = v2 + line.charAt(index);
			index++;
		}
		index++;
		while(line.charAt(index)!='/'){
			t2 = t2 + line.charAt(index);
			index++;
		}
		index++;
		while(line.charAt(index)!=' '){
			n2 = n2 + line.charAt(index);
			index++;
		}
		index++;
		while(line.charAt(index)!='/'){
			v3 = v3 + line.charAt(index);
			index++;
		}
		index++;
		while(line.charAt(index)!='/'){
			t3 = t3 + line.charAt(index);
			index++;
		}
		index++;
		while(index<line.length()){
			n3 = n3 + line.charAt(index);
			index++;
		}
		
		
		double dv1 = Double.parseDouble(v1);
		double dt1 = Double.parseDouble(t1);
		double dn1 = Double.parseDouble(n1);
		Vec3 vert1 = new Vec3(dv1,dt1,dn1);
		double dv2 = Double.parseDouble(v2);
		double dt2 = Double.parseDouble(t2);
		double dn2 = Double.parseDouble(n2);
		Vec3 vert2 = new Vec3(dv2,dt2,dn2);
		double dv3 = Double.parseDouble(v3);
		double dt3 = Double.parseDouble(t3);
		double dn3 = Double.parseDouble(n3);
		Vec3 vert3 = new Vec3(dv3,dt3,dn3);
		return new Triangle(vert1,vert2,vert3);
	}


	/**
	 * @return An ArrayList of all vertices
	 */
	public ArrayList<Vec3> getVertices() {
		return vertices;
	}


	/**
	 * @return An ArrayList of all normals
	 */
	public ArrayList<Vec3> getNormals() {
		return normals;
	}


	/**
	 * @return An ArrayList of all texcoords
	 */
	public ArrayList<Vec2> getTexcoords() {
		return texcoords;
	}


	/**
	 * @return An ArrayList of all faces (triangles)
	 */
	public ArrayList<Triangle> getTriangles() {
		return triangles;
	}
	
	
}
