package model.treatment;

import java.awt.image.BufferedImage;
import java.util.TreeMap;

import config.Config;
import model.Parameter;
import model.mesh.Point3D;
import model.mesh.RaisedPoint;
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

				mapMesh.addBaseRaisedPoint(line, column,
						new Point3D(line * ratioX, Config.BASE_MAP_RAISED_TICKNESS, column * ratioZ));
			}
		}

		for (int a = 0; a < basePointTableY.length; a++) {
			for (int b = 0; b < basePointTableX.length; b++) {
				
				mapMesh.addBaseRaisedPoint(basePointTableX[a], basePointTableY[b],
						new Point3D(basePointTableX[a], Config.BASE_MAP_RAISED_TICKNESS, basePointTableY[b]));

				mapMesh.addBasePoint(basePointTableX[a], basePointTableY[b],
						new Point3D(basePointTableX[a], Config.BASE_MAP_TICKNESS, basePointTableY[b]));
			}
		}

		Config.Debug("-- Indexation des faces en surface de la map");

		for (double line = 0; line < height - 1; line++) {
			for (double column = 0; column < width - 1; column++) {

				TreeMap<Integer, Point3D> setOfVertices = new TreeMap<>();

				setOfVertices.put(0, mapMesh.getSurfacePoint(line + 1, column + 1));
				setOfVertices.put(1, mapMesh.getSurfacePoint(line + 1, column));
				setOfVertices.put(2, mapMesh.getSurfacePoint(line, column));
				setOfVertices.put(3, mapMesh.getSurfacePoint(line, column + 1));

				setOfFaces.put(k++, setOfVertices);

			}
		}
		
		Config.Debug("-- Indexation des faces sur le coté de la map");

		for (double column = 0; column < width - 1; column++) {
			// Left
			TreeMap<Integer, Point3D> setOfVertices1 = new TreeMap<>();

			setOfVertices1.put(0, mapMesh.getSurfacePoint(0, column));
			setOfVertices1.put(1, mapMesh.getBaseRaisedPoint(0, column));
			setOfVertices1.put(2, mapMesh.getBaseRaisedPoint(0, column + 1));
			setOfVertices1.put(3, mapMesh.getSurfacePoint(0, column+1));
			
			setOfFaces.put(k++, setOfVertices1);
			
			// Right
			TreeMap<Integer, Point3D> setOfVertices2 = new TreeMap<>();

			setOfVertices2.put(0, mapMesh.getSurfacePoint(width-1, column+1));
			setOfVertices2.put(1, mapMesh.getBaseRaisedPoint(width-1, column + 1));
			setOfVertices2.put(2, mapMesh.getBaseRaisedPoint(width-1, column));
			setOfVertices2.put(3, mapMesh.getSurfacePoint(width-1, column));
	
			
			setOfFaces.put(k++, setOfVertices2);
		}
	
		for (double line = 0; line < height - 1; line++) {

			// Bottom
			TreeMap<Integer, Point3D> setOfVertices1 = new TreeMap<>();

			setOfVertices1.put(0, mapMesh.getSurfacePoint(line +1, 0));
			setOfVertices1.put(1, mapMesh.getBaseRaisedPoint(line +1, 0));
			setOfVertices1.put(2, mapMesh.getBaseRaisedPoint(line, 0));
			setOfVertices1.put(3, mapMesh.getSurfacePoint(line, 0));
			
			setOfFaces.put(k++, setOfVertices1);
			
			// Top
			TreeMap<Integer, Point3D> setOfVertices2 = new TreeMap<>();

			setOfVertices2.put(0, mapMesh.getSurfacePoint(line, height-1));
			setOfVertices2.put(1, mapMesh.getBaseRaisedPoint(line, height-1));
			setOfVertices2.put(2, mapMesh.getBaseRaisedPoint(line +1, height-1));
			setOfVertices2.put(3, mapMesh.getSurfacePoint(line +1, height-1));
			
			setOfFaces.put(k++, setOfVertices2);
		}
		

		Config.Debug("-- Indexation des faces sous la map");

		for (int a = 0; a < basePointTableY.length - 1; a++) {
			for (int b = 0; b < basePointTableX.length - 1; b++) {

				TreeMap<Integer, Point3D> setOfVertices = new TreeMap<>();

				if (isRaisedFace(a, b, RaisedPoint.RAISED_LIST_POINTS)) {
					setOfVertices.put(0, mapMesh.getBaseRaisedPoint(basePointTableX[a], basePointTableY[b]));
					setOfVertices.put(1, mapMesh.getBaseRaisedPoint(basePointTableX[a + 1], basePointTableY[b]));
					setOfVertices.put(2, mapMesh.getBaseRaisedPoint(basePointTableX[a + 1], basePointTableY[b + 1]));
					setOfVertices.put(3, mapMesh.getBaseRaisedPoint(basePointTableX[a], basePointTableY[b + 1]));
				} else {
					setOfVertices.put(0, mapMesh.getBasePoint(basePointTableX[a], basePointTableY[b]));
					setOfVertices.put(1, mapMesh.getBasePoint(basePointTableX[a + 1], basePointTableY[b]));
					setOfVertices.put(2, mapMesh.getBasePoint(basePointTableX[a + 1], basePointTableY[b + 1]));
					setOfVertices.put(3, mapMesh.getBasePoint(basePointTableX[a], basePointTableY[b + 1]));
				}
				setOfFaces.put(k++, setOfVertices);
			}
		}

		TreeMap<Integer, Point3D> setOfVertices11 = new TreeMap<>();

		setOfVertices11.put(0, mapMesh.getBaseRaisedPoint(0, 0));
		setOfVertices11.put(1, mapMesh.getBasePoint(0, 0));
		setOfVertices11.put(2, mapMesh.getBasePoint(0, basePointTableY[5]));
		setOfVertices11.put(3, mapMesh.getBaseRaisedPoint(0, basePointTableY[5]));
		
		setOfFaces.put(k++, setOfVertices11);
		
		TreeMap<Integer, Point3D> setOfVertices12 = new TreeMap<>();

		setOfVertices12.put(0, mapMesh.getBaseRaisedPoint(0, basePointTableY[6]));
		setOfVertices12.put(1, mapMesh.getBasePoint(0, basePointTableY[6]));
		setOfVertices12.put(2, mapMesh.getBasePoint(0, basePointTableY[11]));
		setOfVertices12.put(3, mapMesh.getBaseRaisedPoint(0, basePointTableY[11]));
		
		setOfFaces.put(k++, setOfVertices12);
		
		TreeMap<Integer, Point3D> setOfVertices21 = new TreeMap<>();

		setOfVertices21.put(0, mapMesh.getBasePoint(width-1, 0));
		setOfVertices21.put(1, mapMesh.getBaseRaisedPoint(width-1, 0));
		setOfVertices21.put(2, mapMesh.getBaseRaisedPoint(width-1, basePointTableY[5]));
		setOfVertices21.put(3, mapMesh.getBasePoint(width-1, basePointTableY[5]));
	
		setOfFaces.put(k++, setOfVertices21);
		
		TreeMap<Integer, Point3D> setOfVertices22 = new TreeMap<>();

		setOfVertices22.put(0, mapMesh.getBasePoint(width-1, basePointTableY[6]));
		setOfVertices22.put(1, mapMesh.getBaseRaisedPoint(width-1, basePointTableY[6]));
		setOfVertices22.put(2, mapMesh.getBaseRaisedPoint(width-1, basePointTableY[11]));
		setOfVertices22.put(3, mapMesh.getBasePoint(width-1, basePointTableY[11]));
		
		setOfFaces.put(k++, setOfVertices22);
		
		TreeMap<Integer, Point3D> setOfVertices31 = new TreeMap<>();

		setOfVertices31.put(0, mapMesh.getBaseRaisedPoint(basePointTableX[5], 0));
		setOfVertices31.put(1, mapMesh.getBasePoint(basePointTableX[5], 0));
		setOfVertices31.put(2, mapMesh.getBasePoint(0, 0));
		setOfVertices31.put(3, mapMesh.getBaseRaisedPoint(0, 0));

		setOfFaces.put(k++, setOfVertices31);
		
		TreeMap<Integer, Point3D> setOfVertices32 = new TreeMap<>();
		
		setOfVertices32.put(0, mapMesh.getBaseRaisedPoint(basePointTableX[11], 0));
		setOfVertices32.put(1, mapMesh.getBasePoint(basePointTableX[11],0));
		setOfVertices32.put(2, mapMesh.getBasePoint(basePointTableX[6], 0));
		setOfVertices32.put(3, mapMesh.getBaseRaisedPoint(basePointTableX[6], 0));
	
		setOfFaces.put(k++, setOfVertices32);
		
		TreeMap<Integer, Point3D> setOfVertices41 = new TreeMap<>();

		setOfVertices41.put(0, mapMesh.getBaseRaisedPoint(0, height-1));
		setOfVertices41.put(1, mapMesh.getBasePoint(0, height-1));
		setOfVertices41.put(2, mapMesh.getBasePoint(basePointTableX[5], height-1));
		setOfVertices41.put(3, mapMesh.getBaseRaisedPoint(basePointTableX[5], height-1));
		
		setOfFaces.put(k++, setOfVertices41);
		
		TreeMap<Integer, Point3D> setOfVertices42 = new TreeMap<>();

		setOfVertices42.put(0, mapMesh.getBaseRaisedPoint(basePointTableX[6], height-1));
		setOfVertices42.put(1, mapMesh.getBasePoint(basePointTableX[6], height-1));
		setOfVertices42.put(2, mapMesh.getBasePoint(basePointTableX[11],height-1));
		setOfVertices42.put(3, mapMesh.getBaseRaisedPoint(basePointTableX[11], height-1));
		
		setOfFaces.put(k++, setOfVertices42);
		
		Config.Debug("-- Creation d'une HE_Mesh -> Creator from quad");

		WB_Quad[] wb_Quads = faceGenerator(setOfFaces, k);

		HE_Mesh he_mesh = new HE_Mesh(new HEC_FromQuads(wb_Quads));
		mapMesh.setHe_mesh(he_mesh);
		return mapMesh;
	}

	private boolean isRaisedFace(int x, int y, List<RaisedPoint> raisedListPoint) {

		RaisedPoint raisedPoint1 = new RaisedPoint(x, y);
		RaisedPoint raisedPoint2 = new RaisedPoint(x, y + 1);
		RaisedPoint raisedPoint3 = new RaisedPoint(x + 1, y + 1);
		RaisedPoint raisedPoint4 = new RaisedPoint(x + 1, y);

		boolean condition = raisedListPoint.contains(raisedPoint1) && raisedListPoint.contains(raisedPoint2)
				&& raisedListPoint.contains(raisedPoint3) && raisedListPoint.contains(raisedPoint4);

		boolean tmp = (x != 2 && x != 8 && y != 2 && y != 8);

		return condition && tmp;
	}

	/**
	 * Methode to generate the defaut point of the base map
	 * 
	 * @param mapSize
	 *            (the width or the height of the map)
	 * @return a double table contains value
	 */
	private double[] generateBasePointTable(double printSize) {

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