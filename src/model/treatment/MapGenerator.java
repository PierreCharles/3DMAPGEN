package model.treatment;

import java.awt.image.BufferedImage;
import java.util.TreeMap;

import config.Config;
import model.Parameter;
import model.mesh.Point3D;
import model.mesh.MapMesh;
import wblut.geom.WB_Polygon;
import wblut.hemesh.HEC_FromPolygons;
import wblut.hemesh.HE_Mesh;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class treatment It is the class for execute the treatment of the task
 * 
 * @author
 *
 */
public class MapGenerator {

	private Parameter parameters;
	private ImageLoader imageLoader;
	private int heightCutNumber, widthCutNumber, heightOfParcel, widthOfParcel;
	ArrayList<Integer> baseFacesList;

	/**
	 * Constructor of a map generator
	 * 
	 * @param parameters
	 * @param imageLoader
	 */
	public MapGenerator(Parameter parameters, ImageLoader imageLoader) {
		this.parameters = parameters;
		this.imageLoader = imageLoader;
		// Add the ID of base face to raised
		baseFacesList = new ArrayList<Integer>(
				Arrays.asList(5, 15, 16, 17, 55, 45, 56, 67, 53, 64, 65, 75, 103, 104, 105, 115, 36, 37, 38, 39, 40, 47,
						48, 49, 50, 51, 58, 59, 60, 61, 62, 69, 70, 71, 72, 73, 80, 81, 82, 83, 84));
	}

	/**
	 * Method to execute treatment
	 * 
	 * @param selectedFileURI
	 * @param parameter
	 * @return
	 */
	public List<MapMesh> executeTreatment() {

		List<MapMesh> parcelList = new ArrayList<>();
		List<BufferedImage> imagesList = cutImage();

		imagesList.forEach((image) -> {
			parcelList.add(parcelToMesh(image));
		});

		return parcelList;
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
	public List<BufferedImage> cutImage() {

		List<BufferedImage> imageList = new ArrayList<>();
		BufferedImage imageBase = imageLoader.getBufferedImage();
		heightCutNumber = (int) Math.ceil(parameters.getImageHeight() / (parameters.getMaxHeightOfPrint() / 10));
		widthCutNumber = (int) Math.ceil(parameters.getImageWidth() / (parameters.getMaxWidthOfPrint() / 10));
		heightOfParcel = (int) Math.floor(imageBase.getHeight() / heightCutNumber);
		widthOfParcel = (int) Math.floor(imageBase.getWidth() / widthCutNumber);

		for (int x = 0; x < widthCutNumber; x++) {
			for (int y = 0; y < heightCutNumber; y++) {
				imageList.add(
						imageBase.getSubimage(x * widthOfParcel, y * heightOfParcel, widthOfParcel, heightOfParcel));
			}
		}

		Config.Debug(imageList.size() + " partelle(s) de " + heightOfParcel + " par : " + widthOfParcel);

		return imageList;
	}

	/**
	 * Allow to obtain the height of a pixel of the loaded image in function of
	 * these coordonate
	 * 
	 * @param line
	 *            y coordonate
	 * @param column
	 *            x coordonate
	 * @param resolution
	 *            Resolution of the height in function of the grey level
	 * @param bufferedImage
	 *            loaded image into application
	 * @return the attempt height for the vertices of the mesh associated at the
	 *         attempt pixel
	 */
	public double getPixelHeight(BufferedImage bufferedImage, double line, double column, double resolution) {
		int pixel = bufferedImage.getRGB((int) Math.floor(line), (int) Math.floor(column));
		int red = (pixel >> 16) & 0xff;
		int green = (pixel >> 8) & 0xff;
		int blue = (pixel) & 0xff;
		int medium = 255 - (red + green + blue) / 3;
		return (resolution * medium) + Config.MAP_ELEVATION;
	}

	/**
	 * Method to convert a parcel to mesh
	 * 
	 * @param bufferedImage
	 * @param max
	 * @param parameter
	 * @return a mesh
	 */
	public MapMesh parcelToMesh(BufferedImage bufferedImage) {

		int k = 0;
		double resolution = parameters.getMeshHeight() / 256;
		double height = bufferedImage.getHeight();
		double width = bufferedImage.getWidth();
		double ratioX = parameters.getMaxWidthOfPrint() / widthOfParcel;
		double ratioZ = parameters.getMaxHeightOfPrint() / heightOfParcel;

		double[] basePointTableX = generateBasePointTable(parameters.getMaxWidthOfPrint(), ratioX);
		double[] basePointTableY = generateBasePointTable(parameters.getMaxHeightOfPrint(), ratioZ);

		MapMesh mapMesh = new MapMesh(height * ratioZ, width * ratioX);
		ArrayList<WB_Polygon> wbPolygonList = new ArrayList<WB_Polygon>();

		Config.Debug("-- Indexation de tous les points de la map");

		// Create a surface coordinates points : line;column
		for (double line = 0; line < height; line++) {
			for (double column = 0; column < width; column++) {
				mapMesh.addSurfacePoint(line, column, new Point3D(line * ratioX,
						getPixelHeight(bufferedImage, line, column, resolution), column * ratioZ));
			}
		}

		for (int a = 0; a < basePointTableY.length; a++) {
			for (int b = 0; b < basePointTableX.length; b++) {
				mapMesh.addBaseRaisedPoint(a, b,
						new Point3D(basePointTableX[a], Config.BASE_MAP_RAISED_TICKNESS, basePointTableY[b]));
				mapMesh.addBasePoint(a, b,
						new Point3D(basePointTableX[a], Config.BASE_MAP_TICKNESS, basePointTableY[b]));
			}
		}

		Config.Debug("-- Creation des faces en surface de la map");

		for (double line = 0; line < height - 1; line++) {
			for (double column = 0; column < width - 1; column++) {
				wbPolygonList.add(new WB_Polygon(
					mapMesh.getSurfacePoint(line + 1, column + 1),
					mapMesh.getSurfacePoint(line + 1, column),
					mapMesh.getSurfacePoint(line, column),
					mapMesh.getSurfacePoint(line, column + 1)));
			}
		}

		// Config.Debug("-- Creation des faces sur le coté de la map");
		Config.Debug("-- Creation des faces sous la map");

		int idFace = 0;
		for (int a = 0; a < basePointTableY.length - 1; a++) {
			for (int b = 0; b < basePointTableX.length - 1; b++) {

				if (baseFacesList.contains(idFace)) {
					wbPolygonList.add(new WB_Polygon(
							mapMesh.getBaseRaisedPoint(a, b), 
							mapMesh.getBaseRaisedPoint(a + 1, b),
							mapMesh.getBaseRaisedPoint(a + 1, b + 1), 
							mapMesh.getBaseRaisedPoint(a, b + 1)));
				} else {
					wbPolygonList.add(new WB_Polygon(
							mapMesh.getBasePoint(a, b), 
							mapMesh.getBasePoint(a + 1, b),
							mapMesh.getBasePoint(a + 1, b + 1), 
							mapMesh.getBasePoint(a, b + 1)));

					if (baseFacesList.contains(idFace + 1) && ((idFace + 1) % (basePointTableY.length - 1)) != 0) { // Inner right side
						wbPolygonList.add(new WB_Polygon(
								mapMesh.getBasePoint(a, b + 1),
								mapMesh.getBasePoint(a + 1, b + 1), 
								mapMesh.getBaseRaisedPoint(a + 1, b + 1),
								mapMesh.getBaseRaisedPoint(a, b + 1)));
					}

					if (baseFacesList.contains(idFace - 1) && (idFace % (basePointTableY.length - 1)) != 0) { // Inner left side
						wbPolygonList.add(new WB_Polygon(
								mapMesh.getBaseRaisedPoint(a + 1, b),
								mapMesh.getBaseRaisedPoint(a, b),
								mapMesh.getBasePoint(a, b),
								mapMesh.getBasePoint(a + 1, b)));
					}

					if (baseFacesList.contains(idFace + (basePointTableX.length - 1))) { // Inner bottom side
						wbPolygonList.add(new WB_Polygon(
								mapMesh.getBasePoint(a + 1, b + 1),
								mapMesh.getBasePoint(a + 1, b), 
								mapMesh.getBaseRaisedPoint(a + 1, b),
								mapMesh.getBaseRaisedPoint(a + 1, b + 1)));
					}

					if (baseFacesList.contains(idFace - (basePointTableX.length - 1))) { // Inner Top side
						wbPolygonList.add(new WB_Polygon(
								mapMesh.getBasePoint(a, b + 1), 
								mapMesh.getBasePoint(a, b),
								mapMesh.getBaseRaisedPoint(a, b), 
								mapMesh.getBaseRaisedPoint(a, b + 1)));
					}
				}
				idFace++;
			}
		}

		Config.Debug("-- Creation d'une HE_Mesh -> Creation à partir de polygones");

		HE_Mesh he_mesh = new HE_Mesh(new HEC_FromPolygons(wbPolygonList));

		mapMesh.setHe_mesh(he_mesh);
		return mapMesh;
	}

	/**
	 * Methode to generate the defaut point of the base map
	 * 
	 * @param mapSize
	 *            (the width or the height of the map)
	 * @return a double table contains value
	 */
	private double[] generateBasePointTable(double printSize, double ratio) {

		double basePointTable[] = new double[12];

		basePointTable[0] = 0;
		basePointTable[1] = (Config.INSIDE_HEIGHT_CLIP / 2) - 1;
		basePointTable[2] = (Config.TOTAL_CLIP_HEIGHT / 2) - 1;
		basePointTable[3] = ((printSize - Config.MIDDLE_SQUARE_MAP_SIZE) / 2) - 1;
		basePointTable[4] = ((printSize - Config.OUTSIDE_WIDTH_CLIP) / 2) - 1;
		basePointTable[5] = ((printSize - Config.INSIDE_WIDTH_CLIP) / 2) - 1;
		basePointTable[6] = ((printSize + Config.INSIDE_WIDTH_CLIP) / 2) - 1;
		basePointTable[7] = ((printSize + Config.OUTSIDE_WIDTH_CLIP) / 2) - 1;
		basePointTable[8] = ((printSize + Config.MIDDLE_SQUARE_MAP_SIZE) / 2) - 1;
		basePointTable[9] = (printSize - (Config.TOTAL_CLIP_HEIGHT / 2)) - 1;
		basePointTable[10] = (printSize - (Config.INSIDE_HEIGHT_CLIP / 2)) - 1;
		basePointTable[11] = printSize - 1;

		return basePointTable;
	}
}