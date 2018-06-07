package util;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import global.GLOBAL;


/**
 * @author Simon
 * Used for buffering the texture image
 * It is very unefficient to create a bufferedImage for each pixel
 * This class only initializes a texture image if it isn't initialized already
 */
public class TextureBuffer {
	
	String name;
	BufferedImage tex = null;
	WritableRaster raster = null;
	private double[] dArray;
	int height;
	int width;
	
	/**
	 * Creates a bufferedImage only if the same wasn't created already
	 * @param name The name of the texture image
	 */
	public void initialize(String name){
		if(!name.equals(this.name)){
			try {
				tex = ImageIO.read(new File(GLOBAL.path.toString()+"/"+name));
				raster = tex.getRaster();
				this.height = tex.getHeight();
				this.width = tex.getWidth();
				this.name = name;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Returns the color of a specified pixel
	 * @param x x value in image coords
	 * @param y y value in image coords
	 * @return Array containing r, g and b as doubles
	 */
	public double[] getPixel(int x, int y){
		return raster.getPixel(x, y, dArray);
	}
	
	/**
	 * @return the height of the texture image
	 */
	public int getHeight(){
		return height;
	}
	
	/**
	 * @return the width of the texture image
	 */
	public int getWidth(){
		return width;
	}
}
