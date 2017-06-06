package model;

/**
 * She allows the user to choose her own parameters as the treatment of the
 * image according to the size of her printer.
 * 
 * @author petit
 *
 */
public class Parameter {

	private double imageWidth, imageHeight, meshHeight, maxWidthOfPrint, maxHeightOfPrint;

	/**
	 * Parameter construtor
	 * @param height
	 * @param width
	 * @param meshHeight
	 * @param maxHeightOfPrint
	 * @param maxWidthOfPrint
	 */
	public Parameter(double height, double width,double meshHeight, double maxHeightOfPrint,
			double maxWidthOfPrint) {
		this.imageHeight = height;
		this.imageWidth = width;
		this.meshHeight = meshHeight;
		this.maxHeightOfPrint = maxHeightOfPrint;
		this.maxWidthOfPrint = maxWidthOfPrint;
	}
	
	
	/**
	 * Getter of the with of image
	 * 
	 * @return the with of image
	 */
	public double getImageWidth() {
		return imageWidth;
	}

	/**
	 * Setter of the with of image
	 * 
	 * @param imageWidth
	 */
	public void setImageWidth(double imageWidth) {
		this.imageWidth = imageWidth;
	}

	/**
	 * Getter of the height of image
	 * 
	 * @return the height of the image
	 */
	public double getImageHeight() {
		return imageHeight;
	}

	/**
	 * Setter of the image height
	 * 
	 * @param imageHeight : the height of the image
	 */
	public void setImageHeight(double imageHeight) {
		this.imageHeight = imageHeight;
	}

	/**
	 * Getter of the height of the mesh
	 * 
	 * @return the height of the mesh
	 */
	public double getMeshHeight() {
		return meshHeight;
	}

	/**
	 * Setter of the height of the mesh
	 * 
	 * @param meshHeight
	 */
	public void setMeshHeight(double meshHeight) {
		this.meshHeight = meshHeight;
	}

	/**
	 * Getter of the max width of the print
	 * 
	 * @return the max width of the print
	 */
	public double getMaxWidthOfPrint() {
		return maxWidthOfPrint;
	}

	/**
	 * Setter of the max width of the print
	 * 
	 * @param maxWidthOfPrint : the max width of the print
	 */
	public void setMaxWidthOfPrint(double maxWidthOfPrint) {
		this.maxWidthOfPrint = maxWidthOfPrint;
	}

	/**
	 * Getter of the max height of the print
	 * 
	 * @return the max height of the print
	 */
	public double getMaxHeightOfPrint() {
		return maxHeightOfPrint;
	}

	/**
	 * Setter of the max height of the print
	 * 
	 * @param maxHeightOfPrint
	 */
	public void setMaxHeightOfPrint(double maxHeightOfPrint) {
		this.maxHeightOfPrint = maxHeightOfPrint;
	}

}
