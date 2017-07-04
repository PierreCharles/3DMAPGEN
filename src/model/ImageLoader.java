package model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import config.Config;
import javafx.scene.image.Image;

/**
 * class Load Class use for load an image into the application
 * 
 * @author
 *
 */
public class ImageLoader {

	private BufferedImage bufferedImage;
	private double height, width, ratioHeight, ratioWidth;
	private Image image;
	private String imagePath;

	/**
	 * Constructor of a loaded image
	 * 
	 * @param file
	 */
	public ImageLoader(File file) {
		try {
			this.imagePath = file.toURI().toString();
			this.image = new Image(imagePath);
			this.height = this.image.getHeight();
			this.width = this.image.getWidth();
			this.bufferedImage = ImageIO.read(file);
			calculateRatio();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Config.Debug("Chargement de l'image : hauteur : " + height + " largeur : " + width);
	}

	private void calculateRatio() {
		try {
			this.ratioWidth = this.width / this.height;
			this.ratioHeight = this.height / this.width;
			System.out.println("ratioHeight : "+ratioHeight+" ratioWidth : "+ ratioWidth);
		} catch (Exception e) {
			System.out.println("division by zero exception -> " + e.getMessage());
		}
	}

	/**
	 * Getter of the height ratio
	 * 
	 * @return
	 */
	public double getRatioHeight() {
		return ratioHeight;
	}

	/**
	 * Getter of the width ratio
	 * 
	 * @return
	 */
	public double getRatioWidth() {
		return ratioWidth;
	}

	/**
	 * Getter of the image object
	 * 
	 * @return
	 */
	public Image getImage() {
		return image;
	}

	/**
	 * Setter of the image object
	 * 
	 * @param image
	 */
	public void setImage(Image image) {
		this.image = image;
	}

	/**
	 * Method to get the path of the current image
	 * 
	 * @return
	 */
	public String getImagePath() {
		return imagePath;
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
	public double getHeight() {
		return height;
	}

	/**
	 * Getter of the width of the image
	 * 
	 * @return the image width
	 */
	public double getWidth() {
		return width;
	}
}