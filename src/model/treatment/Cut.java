package model.treatment;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import config.Config;

/**
 * Class Cut Processing to the calculate of necessary cuts takes care
 * 
 * @author alexi
 */
public class Cut {

	private static int HeightCutNumber, WidthCutNumber, HeightOfPartel, WidthOfParcel;

	/**
	 * Getter of the height of the parcel
	 * 
	 * @return the height of the parcel
	 */
	public static int getHeightOfParcel() {
		return HeightOfPartel;
	}

	/**
	 * Getter of the width of the parcel
	 * 
	 * @return the width of the parcel
	 */
	public static int getWidthOfParcel() {
		return WidthOfParcel;
	}

	/**
	 * Getter of the height cut number
	 * 
	 * @return the height cut number
	 */
	public static int getHeightCutNumber() {
		return HeightCutNumber;
	}

	/**
	 * Getter of the width cut number
	 * 
	 * @return the width cut number
	 */
	public static int getWidthCutNumber() {
		return WidthCutNumber;
	}

	/**
	 * Method for cut the image with some parameters
	 * 
	 * @param imageLoaded
	 * @param expectedWidth
	 * @param expectedHeight
	 * @param maxWidthOfPrint
	 * @param maxHeightOfPrint
	 * @return a list of cut images
	 */
	public static List<BufferedImage> cutImage(Load imageLoaded, double expectedWidth, double expectedHeight,
			double maxWidthOfPrint, double maxHeightOfPrint) {
		List<BufferedImage> imageList = new ArrayList<>();
		BufferedImage imageBase = imageLoaded.getBufferedImage();
		Cut.HeightCutNumber = (int) Math.ceil(expectedHeight / (maxHeightOfPrint / 10));
		Cut.WidthCutNumber = (int) Math.ceil(expectedWidth / (maxWidthOfPrint / 10));
		Cut.HeightOfPartel = (int) Math.floor(imageBase.getHeight() / getHeightCutNumber());
		Cut.WidthOfParcel = (int) Math.floor(imageBase.getWidth() / getWidthCutNumber());

		if (Config.DEBUG) {
			System.out.println("Hauteur de la partelle : " + getHeightOfParcel());
			System.out.println("Largeur de la partelle : " + getWidthOfParcel());
		}

		for (int x = 0; x < getWidthCutNumber(); x++) {
			for (int y = 0; y < getHeightCutNumber(); y++) {
				imageList.add(imageBase.getSubimage(x * getWidthOfParcel(), y * getHeightOfParcel(), getWidthOfParcel(),
						getHeightOfParcel()));
			}
		}
		return imageList;
	}
}
