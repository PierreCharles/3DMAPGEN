package model.treatment;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import config.Config;
import javafx.stage.FileChooser;

/**
 * class Load Class use for load an image into the application
 * 
 * @author
 *
 */
public class Load {

	BufferedImage bufferedImage = null;
	private File file;
	private int height;
	private int width;
	int[][] treatedImage = new int[width][height];

	/**
	 * Methode for load an image file
	 * 
	 * @param file
	 */
	public Load(File file) {
		this.file = file;
	}

	/**
	 * Getter of treated image list
	 * 
	 * @return the treated image list
	 */
	public int[][] getTreatedImage() {
		return treatedImage;
	}

	/**
	 * Getter of the buffered image
	 * 
	 * @return the buffered image
	 */
	public BufferedImage getBufferedImage() {
		return bufferedImage;
	}

	/**
	 * Getter of the image height
	 * 
	 * @return the height of the image
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Getter of the width of the image
	 * 
	 * @return the image width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Method for add an image
	 */
	public void addImage() {
		try {
			bufferedImage = ImageIO.read(file);
			this.height = bufferedImage.getHeight();
			this.width = bufferedImage.getWidth();

			if(Config.DEBUG){
				System.out.println("image chargée : hauteur : " + height + " largeur : " + width);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}