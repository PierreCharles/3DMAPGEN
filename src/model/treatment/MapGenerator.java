package model.treatment;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.TreeMap;

import javax.imageio.ImageIO;

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
	private int heightCutNumber, widthCutNumber, heightOfPartel, widthOfParcel;

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
		heightOfPartel = (int) Math.floor(imageBase.getHeight() / heightCutNumber);
		widthOfParcel = (int) Math.floor(imageBase.getWidth() / widthCutNumber);

		for (int x = 0; x < widthCutNumber; x++) {
			for (int y = 0; y < heightCutNumber; y++) {
				imageList.add(
						imageBase.getSubimage(x * widthOfParcel, y * heightOfPartel, widthOfParcel, heightOfPartel));
			}
		}

		Config.Debug("Découpage en : " + imageList.size() + " partelle(s)");
		Config.Debug("Hauteur d'une partelle : " + heightOfPartel + " --- Largeur d'une partelle : " + widthOfParcel);

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
		return (resolution * medium) + 5;
	}

	/**
	 * Method to know if have to raised
	 * 
	 * @param bufferedImage
	 * @param line
	 * @param column
	 * @param beginWidth
	 * @param endWidth
	 * @param beginHeight
	 * @param endHeight
	 * @return a boolean
	 */
	public boolean haveToRaised(double height, double width, double line, double column) {

		boolean condition1, topClipCondition, condition3, condition4, condition5;

		// Rectangle base support
		condition1 = column > (width - Config.SQUARE_ID_MAP_SIZE) / 2
				&& column < (width + Config.SQUARE_ID_MAP_SIZE) / 2 && line > (height - Config.SQUARE_ID_MAP_SIZE) / 2
				&& line < (height + Config.SQUARE_ID_MAP_SIZE) / 2;

		// Top clip support
		topClipCondition = (column >= (width - Config.BASE_MAP_SIZE) / 2 && column <= (width + Config.BASE_MAP_SIZE) / 2
				&& line <= (Config.BASE_MAP_SIZE + Config.CLIP_HEIGHT_INSIDE / 2)
				|| column >= (width - Config.WIDTH_CLIP_OUTSIDE) / 2
						&& column <= (width + Config.WIDTH_CLIP_OUTSIDE) / 2
						&& line <= (Config.BASE_MAP_SIZE + Config.CLIP_HEIGHT / 2));

		// Left clip support
		condition3 = column <= Config.BASE_MAP_SIZE && line >= (height - Config.BASE_MAP_SIZE) / 2
				&& line <= (height + Config.BASE_MAP_SIZE) / 2;

		// Bottom clip support
		condition4 = column >= (width - Config.BASE_MAP_SIZE) / 2 && column <= (width + Config.BASE_MAP_SIZE) / 2
				&& line >= height - Config.BASE_MAP_SIZE;

		// Right clip support
		condition5 = line >= (height - Config.BASE_MAP_SIZE) / 2 && line <= (height + Config.BASE_MAP_SIZE) / 2
				&& column >= width - Config.BASE_MAP_SIZE;

		return condition1 || topClipCondition || condition3 || condition4 || condition5;
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

		try {
			File outputfile = new File("saved.png");
			ImageIO.write(bufferedImage, "png", outputfile);
		} catch (IOException e) {

		}

		double resolution = parameters.getMeshHeight() / 256;
		double height = bufferedImage.getHeight();
		double width = bufferedImage.getWidth();
		double ratioX = parameters.getMaxWidthOfPrint() / widthOfParcel;
		double ratioZ = parameters.getMaxHeightOfPrint() / heightOfPartel;
		double tickness = 5;

		Config.Debug("height : " + height + " ratioZ: " + ratioZ + " width: " + width + " ratioX : " + ratioX);

		MapMesh mapMesh = new MapMesh(height * ratioZ, width * ratioX);
		TreeMap<Integer, TreeMap<Integer, Point3D>> setOfFaces = new TreeMap<>();

		int k = 0;
		Config.Debug("-- Indexation des points de la map");

		// Create a surface coordinates points : line;column
		for (double line = 0; line < height; line++) {
			for (double column = 0; column < width; column++) {
				mapMesh.addVertices(line, column, new Point3D(line * ratioX,
						getPixelHeight(bufferedImage, line, column, resolution), column * ratioZ));
			}
		}

		// Create a point coordinate base : line;column
		for (double line = 0; line < height; line++) {
			for (double column = 0; column < width; column++) {
				if (haveToRaised(height, width, line, column)) {
					mapMesh.addVerticesBase(line, column, new Point3D(line * ratioX, tickness, column * ratioZ));
				} else {
					mapMesh.addVerticesBase(line, column, new Point3D(line * ratioX, 2, column * ratioZ));
				}
			}
		}

		Config.Debug("-- Indexation des faces de la map");

		for (double line = 0; line < height; line++) {
			for (double column = 0; column < width; column++) {

				// TOP: Creation of top side
				if (line == 0 && column != 0) {
					TreeMap<Integer, Point3D> setOfVertices1 = new TreeMap<>();

					setOfVertices1.put(0, mapMesh.getSurfacePoint(line, column));
					setOfVertices1.put(1, mapMesh.getSurfacePoint(line, column - 1));
					setOfVertices1.put(2, mapMesh.getBasePoint(line, column - 1));
					setOfVertices1.put(3, mapMesh.getBasePoint(line, column));

					setOfFaces.put(k++, setOfVertices1);
				}

				// BOTTOM : Creation of the bottom side
				if (line == height - 1 && column != 0 && column != width) {

					TreeMap<Integer, Point3D> setOfVertices1 = new TreeMap<>();

					setOfVertices1.put(0, mapMesh.getBasePoint(line, column));
					setOfVertices1.put(1, mapMesh.getBasePoint(line, column - 1));
					setOfVertices1.put(2, mapMesh.getSurfacePoint(line, column - 1));
					setOfVertices1.put(3, mapMesh.getSurfacePoint(line, column));

					setOfFaces.put(k++, setOfVertices1);
				}

				// LEFT : Creation of the left side
				if (column == 0 && line != 0) {

					TreeMap<Integer, Point3D> setOfVertices1 = new TreeMap<>();

					setOfVertices1.put(0, mapMesh.getBasePoint(line, column));
					setOfVertices1.put(1, mapMesh.getBasePoint(line - 1, column));
					setOfVertices1.put(2, mapMesh.getSurfacePoint(line - 1, column));
					setOfVertices1.put(3, mapMesh.getSurfacePoint(line, column));

					setOfFaces.put(k++, setOfVertices1);
				}

				// RIGHT : Creation of the right side
				if (column == width - 1 && line != 0 && line != height) {

					TreeMap<Integer, Point3D> setOfVertices1 = new TreeMap<>();
					TreeMap<Integer, Point3D> setOfVertices2 = new TreeMap<>();
					TreeMap<Integer, Point3D> setOfVertices3 = new TreeMap<>();

					setOfVertices1.put(0, mapMesh.getSurfacePoint(line, column));
					setOfVertices1.put(1, mapMesh.getSurfacePoint(line - 1, column));
					setOfVertices1.put(2, mapMesh.getBasePoint(line - 1, column));
					setOfVertices1.put(3, mapMesh.getBasePoint(line, column));

					setOfVertices2.put(0, mapMesh.getSurfacePoint(line, column - 1));
					setOfVertices2.put(1, mapMesh.getSurfacePoint(line - 1, column - 1));
					setOfVertices2.put(2, mapMesh.getSurfacePoint(line - 1, column));
					setOfVertices2.put(3, mapMesh.getSurfacePoint(line, column));

					setOfVertices3.put(0, mapMesh.getBasePoint(line, column - 1));
					setOfVertices3.put(1, mapMesh.getBasePoint(line, column));
					setOfVertices3.put(2, mapMesh.getBasePoint(line - 1, column));
					setOfVertices3.put(3, mapMesh.getBasePoint(line - 1, column - 1));

					setOfFaces.put(k++, setOfVertices1);
					setOfFaces.put(k++, setOfVertices2);
					setOfFaces.put(k++, setOfVertices3);

				}

				// CENTER : Creation of center faces (base and surface)
				if (line > 0 && line < height && column > 0 && column < width - 1) {

					TreeMap<Integer, Point3D> setOfVertices1 = new TreeMap<>();
					TreeMap<Integer, Point3D> setOfVertices2 = new TreeMap<>();

					setOfVertices1.put(0, mapMesh.getSurfacePoint(line, column));
					setOfVertices1.put(1, mapMesh.getSurfacePoint(line, column - 1));
					setOfVertices1.put(2, mapMesh.getSurfacePoint(line - 1, column - 1));
					setOfVertices1.put(3, mapMesh.getSurfacePoint(line - 1, column));

					setOfVertices2.put(0, mapMesh.getBasePoint(line - 1, column));
					setOfVertices2.put(1, mapMesh.getBasePoint(line - 1, column - 1));
					setOfVertices2.put(2, mapMesh.getBasePoint(line, column - 1));
					setOfVertices2.put(3, mapMesh.getBasePoint(line, column));

					setOfFaces.put(k++, setOfVertices1);
					setOfFaces.put(k++, setOfVertices2);
				}
			}
		}

		Config.Debug("-- Création des faces de la map");
		k--;
		WB_Quad wb_quads[] = new WB_Quad[k];

		for (int key = 0; key < k; key++) {
			wb_quads[key] = new WB_Quad(setOfFaces.get(key).get(0), setOfFaces.get(key).get(1),
					setOfFaces.get(key).get(2), setOfFaces.get(key).get(3));
		}

		Config.Debug("-- Creation d'une HE_Mesh");

		HE_Mesh he_mesh = new HE_Mesh(new HEC_FromQuads(wb_quads));
		mapMesh.setHe_mesh(he_mesh);
		return mapMesh;

	}

	/**
	 * Method to get the number of clip
	 * 
	 * @param cutWidthNumber
	 * @param cutHeightNumber
	 * @return an integer
	 */
	public Integer calculateNumberOfClip(int cutWidthNumber, int cutHeightNumber) {
		return (cutWidthNumber - 1) + (2 * cutWidthNumber - 1) * (cutHeightNumber - 1);
	}

}