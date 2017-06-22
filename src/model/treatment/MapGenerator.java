package model.treatment;

import java.awt.image.BufferedImage;
import java.util.TreeMap;

import config.Config;
import model.Parameter;
import model.mesh.Point3D;
import model.mesh.MapMesh;
import wblut.geom.WB_Quad;
import wblut.hemesh.HEC_FromQuads;
import wblut.hemesh.HE_Mesh;

import java.util.ArrayList;
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

	/**
	 * Constructor of a map generator
	 * 
	 * @param parameters
	 * @param imageLoader
	 */
	public MapGenerator(Parameter parameters, ImageLoader imageLoader) {
		this.parameters = parameters;
		this.imageLoader = imageLoader;
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

		Config.Debug("Découpage en : " + imageList.size() + " partelle(s). Hauteur d'une parcelle : " + heightOfParcel
				+ " largeur d'une parcelle : " + widthOfParcel);

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
		return (resolution * medium) + Config.BASE_MAP_RAISED_TICKNESS;
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

		double resolution = parameters.getMeshHeight() / 256;
		double height = bufferedImage.getHeight();
		double width = bufferedImage.getWidth();
		double ratioX = parameters.getMaxWidthOfPrint() / widthOfParcel;
		double ratioZ = parameters.getMaxHeightOfPrint() / heightOfParcel;

		MapMesh mapMesh = new MapMesh(height * ratioZ, width * ratioX);
		TreeMap<Integer, TreeMap<Integer, Point3D>> setOfFaces = new TreeMap<>();

		double[] basePointTableX = generateBasePointTable(parameters.getMaxWidthOfPrint());
		double[] basePointTableY = generateBasePointTable(parameters.getMaxHeightOfPrint());

		int k = 0;
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
				mapMesh.addBasePoint(basePointTableY[a], basePointTableX[b],
						new Point3D(basePointTableY[a], Config.BASE_MAP_RAISED_TICKNESS, basePointTableX[b]));
				mapMesh.addBaseRaisedPoint(basePointTableY[a], basePointTableX[b],
						new Point3D(basePointTableY[a], Config.BASE_MAP_TICKNESS, basePointTableX[b]));
			}
		}

		Config.Debug("-- Indexation des faces en surface de la map");

		for (double line = 1; line < height; line++) {
			for (double column = 1; column < width; column++) {

				TreeMap<Integer, Point3D> setOfVertices1 = new TreeMap<>();

				setOfVertices1.put(0, mapMesh.getSurfacePoint(line, column));
				setOfVertices1.put(1, mapMesh.getSurfacePoint(line, column - 1));
				setOfVertices1.put(2, mapMesh.getSurfacePoint(line - 1, column - 1));
				setOfVertices1.put(3, mapMesh.getSurfacePoint(line - 1, column));

				setOfFaces.put(k++, setOfVertices1);
			}
		}

		Config.Debug("-- Indexation des faces sous la map");

		for (int a = 0; a < basePointTableY.length - 1; a++) {
			for (int b = 0; b < basePointTableX.length - 1; b++) {

				TreeMap<Integer, Point3D> setOfVertices1 = new TreeMap<>();
				TreeMap<Integer, Point3D> setOfVertices2 = new TreeMap<>();

				setOfVertices1.put(0, mapMesh.getBaseRaisedPoint(basePointTableY[a], basePointTableX[b]));
				setOfVertices1.put(1, mapMesh.getBaseRaisedPoint(basePointTableY[a + 1], basePointTableX[b]));
				setOfVertices1.put(2, mapMesh.getBaseRaisedPoint(basePointTableY[a + 1], basePointTableX[b + 1]));
				setOfVertices1.put(3, mapMesh.getBaseRaisedPoint(basePointTableY[a], basePointTableX[b + 1]));

				setOfVertices2.put(0, mapMesh.getBasePoint(basePointTableY[a], basePointTableX[b]));
				setOfVertices2.put(1, mapMesh.getBasePoint(basePointTableY[a + 1], basePointTableX[b]));
				setOfVertices2.put(2, mapMesh.getBasePoint(basePointTableY[a + 1], basePointTableX[b + 1]));
				setOfVertices2.put(3, mapMesh.getBasePoint(basePointTableY[a], basePointTableX[b + 1]));

				setOfFaces.put(k++, setOfVertices1);
				setOfFaces.put(k++, setOfVertices2);
			}
		}

		WB_Quad[] wb_Quads = faceGenerator(setOfFaces, k);

		Config.Debug("-- Creation d'une HE_Mesh");
		HE_Mesh he_mesh = new HE_Mesh(new HEC_FromQuads(wb_Quads));
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
	private double[] generateBasePointTable(double printSize) {

		// TODO clear this and add ratioZ !!!!!!
		// duplicate floowing code line and replace ratioX by ratioZ
		double basePointTable[] = new double[12];

		basePointTable[0] = 0;
		basePointTable[1] = (Config.INSIDE_HEIGHT_CLIP / 2);
		basePointTable[2] = (Config.TOTAL_CLIP_HEIGHT / 2);
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

	/**
	 * Methode to generate all face with a ordered list of 3D points
	 * 
	 * @param setOfFaces
	 *            the ordered list of 3D point
	 * @param k
	 *            the number of faces
	 * @return an WB_Quad array
	 */
	private WB_Quad[] faceGenerator(TreeMap<Integer, TreeMap<Integer, Point3D>> setOfFaces, int k) {

		Config.Debug("-- Création des faces de la map");

		WB_Quad wb_quads[] = new WB_Quad[k];

		for (int key = 0; key < k; key++) {
			wb_quads[key] = new WB_Quad(setOfFaces.get(key).get(0), setOfFaces.get(key).get(1),
					setOfFaces.get(key).get(2), setOfFaces.get(key).get(3));
		}
		return wb_quads;

	}
}