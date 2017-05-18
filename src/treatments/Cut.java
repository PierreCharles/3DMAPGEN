package treatments;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Class Cut Processing to the calculate of necessary cuts takes care
 * 
 * @author alexi
 */
public class Cut {

	private static int heightCutNumber;
	private static int widthCutNumber;
	private static int heightOfParcel;
	private static int widthOfParcel;

	/**
	 * Getter of the height of the parcel
	 * 
	 * @return the height of the parcel
	 */
	public static int getHeightOfParcel() {
		return heightOfParcel;
	}

	/**
	 * Getter of the width of the parcel
	 * 
	 * @return the width of the parcel
	 */
	public static int getWidthOfParcel() {
		return widthOfParcel;
	}

	/**
	 * Getter of the height cut number
	 * 
	 * @return the height cut number
	 */
	public static int getHeightCutNumber() {
		return heightCutNumber;
	}

	/**
	 * Getter of the width cut number
	 * 
	 * @return the width cut number
	 */
	public static int getWidthCutNumber() {
		return widthCutNumber;
	}

	/**
	 * Method for cut the image with some parameters
	 * 
	 * @param load
	 * @param expectedWidth
	 * @param expectedHeight
	 * @param maxWidthOfPrint
	 * @param maxHeightOfPrint
	 * @return a list of cut images
	 */
	public static List<BufferedImage> cutImage(Load load, double expectedWidth, double expectedHeight,
			double maxWidthOfPrint, double maxHeightOfPrint) {
		List<BufferedImage> imageList = new ArrayList<>();
		BufferedImage imageBase = load.getBufferedImage();
		Cut.heightCutNumber = (int) Math.ceil(expectedHeight / (maxHeightOfPrint / 10));
		Cut.widthCutNumber = (int) Math.ceil(expectedWidth / (maxWidthOfPrint / 10));
		Cut.heightOfParcel = (int) Math.floor(imageBase.getHeight() / getHeightCutNumber());
		Cut.widthOfParcel = (int) Math.floor(imageBase.getWidth() / getWidthCutNumber());
		System.out.println("Height of parcel : " + getHeightOfParcel());
		System.out.println("Width of parcel : " + getWidthOfParcel());

		for (int x = 0; x < getWidthCutNumber(); x++) {
			for (int y = 0; y < getHeightCutNumber(); y++) {
				imageList.add(imageBase.getSubimage(x * getWidthOfParcel(), y * getHeightOfParcel(), getWidthOfParcel(),
						getHeightOfParcel()));
			}
		}
		return imageList;
	}
}
