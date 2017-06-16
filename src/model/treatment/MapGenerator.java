package model.treatment;

import java.awt.image.BufferedImage;
import java.util.TreeMap;

import config.Config;
import model.Parameter;
import model.mesh.Point3D;
import model.mesh.MapMesh;
import wblut.geom.WB_Triangle;
import wblut.hemesh.HEC_FromTriangles;
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
		// Config.Debug("Nombre de découpe en largeur : " + widthCutNumber + "
		// --- Nombre de découpe en hauteur : "+ heightCutNumber);

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
		int pixel = bufferedImage.getRGB((int) Math.floor(column), (int) Math.floor(line));
		int red = (pixel >> 16) & 0xff;
		int green = (pixel >> 8) & 0xff;
		int blue = (pixel) & 0xff;
		int medium = 255 - (red + green + blue) / 3;
		return (resolution * medium) + 5;
	}

	/**
	 * Getter of the height cut number
	 * 
	 * @return the height cut number : int
	 */
	public int getHeightCutNumber() {
		return heightCutNumber;
	}

	/**
	 * Getter of the width cut number
	 * 
	 * @return the width cut numner : int
	 */
	public int getWidthCutNumber() {
		return widthCutNumber;
	}

	/**
	 * Getter of the height of the partel
	 * 
	 * @return height of the partel : int
	 */
	public int getHeightOfPartel() {
		return heightOfPartel;
	}

	/**
	 * Getter of the width of the partel
	 * 
	 * @return width of the parcel : int
	 */
	public int getWidthOfPartel() {
		return widthOfParcel;
	}

	/**
	 * Method to know if an edge is a top edge
	 * 
	 * @param line
	 * @param column
	 * @return a boolean (true if an edge is a top edge)
	 */
	private boolean isTopEdge(double line, double column) {
		return (line == 0 && column != 0);
	}

	/**
	 * Method to know if an edge is a left edge
	 * 
	 * @param ligne
	 * @param column
	 * @return a boolean (true if an edge is a left edge)
	 */
	private boolean isLeftEdge(double ligne, double column) {
		return (column == 0 && ligne != 0);
	}

	/**
	 * Method to know if an edge is a right edge
	 * 
	 * @param line
	 * @param column
	 * @param width
	 * @param height
	 * @return a boolean (true if an edge is a right edge)
	 */
	private boolean isRightEdge(double line, double column, double width, double height) {
		return (column == width && line != 0 && line != height);
	}

	/**
	 * Method to know if an edge is a bottom edge
	 * 
	 * @param line
	 * @param column
	 * @param width
	 * @param height
	 * @return a boolean (true if an edge is a bottom edge)
	 */
	private boolean isBottomEdge(double line, double column, double width, double height) {
		return (line == height && column != 0 && column != width);
	}

	/**
	 * Method to know if that is centered
	 * 
	 * @param line
	 * @param column
	 * @param width
	 * @param height
	 * @return a boolean (true if is centered)
	 */
	private boolean isCenter(double line, double column, double width, double height) {
		return (0 < line && line < height && 0 < column && column < width);
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
		double height = bufferedImage.getHeight() - 1;
		double width = bufferedImage.getWidth() - 1;
		double ratioX = parameters.getMaxWidthOfPrint() / widthOfParcel;
		double ratioZ = parameters.getMaxHeightOfPrint() / heightOfPartel;
		double tickness = 5;
		double beginWidth = 0.1 * width, endWidth = 0.9 * width, beginHeight = 0.1 * height, endHieght = 0.9 * height;

		Config.Debug("height : " + height + " ratioZ: " + ratioZ + " width: " + width + " ratioX : " + ratioX);

		Point3D wb_coords[] = new Point3D[(int) (2 * width * height)];
		MapMesh mapMesh = new MapMesh(height * ratioZ, width * ratioX);
		TreeMap<Integer, TreeMap<Integer, Point3D>> setOfFaces = new TreeMap<>();

		int i = 0, k = 0;
		
		Config.Debug("-- Creation des points de la map");

		// Create a surface coordinates points : line;column
		for (double line = 0; line < height; line++) {
			for (double column = 0; column < width; column++) {
				wb_coords[i] = new Point3D(line * ratioX, getPixelHeight(bufferedImage, line, column, resolution),
						column * ratioZ);
				mapMesh.addVertices(line, column, wb_coords[i]);
				i++;
			}
		}

		// Create a point coordinate base : line;column
		for (double line = 0; line < height; line++) {
			for (double column = 0; column < width; column++) {
				if (haveToRaised(bufferedImage, line, column, beginWidth, endWidth, beginHeight, endHieght)) {
					wb_coords[i] = new Point3D(line * ratioX, tickness, column * ratioZ);
				} else {
					wb_coords[i] = new Point3D(line * ratioX, 2, column * ratioZ);
				}
				mapMesh.addVerticesBase(line, column, wb_coords[i]);
				i++;
			}
		}

		Config.Debug("-- Génération des faces de la map");
		
		for (double line = 0; line < height; line++) {
			for (double column = 0; column < width; column++) {

				// Creation of top side
				if (isTopEdge(line, column)) {

					TreeMap<Integer, Point3D> setOfVertices1 = new TreeMap<>();
					TreeMap<Integer, Point3D> setOfVertices2 = new TreeMap<>();

					setOfVertices1.put(0, mapMesh.getSurfacePoint(line, column));
					setOfVertices1.put(1, mapMesh.getBasePoint(line, column));
					setOfVertices1.put(2, mapMesh.getBasePoint(line, column - 1));

					setOfVertices2.put(0, mapMesh.getBasePoint(line, column - 1));
					setOfVertices2.put(1, mapMesh.getSurfacePoint(line, column - 1));
					setOfVertices2.put(2, mapMesh.getSurfacePoint(line, column));

					setOfFaces.put(k++, setOfVertices1);
					setOfFaces.put(k++, setOfVertices2);
				}

				// Creation of the bottom side
				if (isBottomEdge(line, column, width, height - 1)) {
																		
					TreeMap<Integer, Point3D> setOfVertices1 = new TreeMap<>();
					TreeMap<Integer, Point3D> setOfVertices2 = new TreeMap<>();

					setOfVertices1.put(0, mapMesh.getBasePoint(line, column - 1));
					setOfVertices1.put(1, mapMesh.getBasePoint(line, column));
					setOfVertices1.put(2, mapMesh.getSurfacePoint(line, column));

					setOfVertices2.put(0, mapMesh.getSurfacePoint(line, column));
					setOfVertices2.put(1, mapMesh.getSurfacePoint(line, column - 1));
					setOfVertices2.put(2, mapMesh.getBasePoint(line, column - 1));

					setOfFaces.put(k++, setOfVertices1);
					setOfFaces.put(k++, setOfVertices2);
				}

				if (isLeftEdge(line, column)) {

					TreeMap<Integer, Point3D> setOfVertices1 = new TreeMap<>();
					TreeMap<Integer, Point3D> setOfVertices2 = new TreeMap<>();
					TreeMap<Integer, Point3D> setOfVertices3 = new TreeMap<>();
					TreeMap<Integer, Point3D> setOfVertices4 = new TreeMap<>();

					setOfVertices1.put(0, mapMesh.getSurfacePoint(line, column));
					setOfVertices1.put(1, mapMesh.getSurfacePoint(line - 1, column + 1));
					setOfVertices1.put(2, mapMesh.getSurfacePoint(line - 1, column));

					setOfVertices2.put(0, mapMesh.getBasePoint(line - 1, column));
					setOfVertices2.put(1, mapMesh.getBasePoint(line - 1, column + 1));
					setOfVertices2.put(2, mapMesh.getBasePoint(line, column));

					setOfVertices3.put(0, mapMesh.getSurfacePoint(line, column));
					setOfVertices3.put(1, mapMesh.getSurfacePoint(line - 1, column));
					setOfVertices3.put(2, mapMesh.getBasePoint(line, column));

					setOfVertices4.put(0, mapMesh.getSurfacePoint(line - 1, column));
					setOfVertices4.put(1, mapMesh.getBasePoint(line - 1, column));
					setOfVertices4.put(2, mapMesh.getBasePoint(line, column));

					setOfFaces.put(k++, setOfVertices1);
					setOfFaces.put(k++, setOfVertices2);
					setOfFaces.put(k++, setOfVertices3);
					setOfFaces.put(k++, setOfVertices4);
				}

				if (isRightEdge(line, column, width - 1, height)) {

					TreeMap<Integer, Point3D> setOfVertices1 = new TreeMap<>();
					TreeMap<Integer, Point3D> setOfVertices2 = new TreeMap<>();
					TreeMap<Integer, Point3D> setOfVertices3 = new TreeMap<>();
					TreeMap<Integer, Point3D> setOfVertices4 = new TreeMap<>();

					setOfVertices1.put(0, mapMesh.getSurfacePoint(line, column));
					setOfVertices1.put(1, mapMesh.getSurfacePoint(line - 1, column));
					setOfVertices1.put(2, mapMesh.getSurfacePoint(line, column - 1));

					setOfVertices2.put(0, mapMesh.getBasePoint(line, column - 1));
					setOfVertices2.put(1, mapMesh.getBasePoint(line - 1, column));
					setOfVertices2.put(2, mapMesh.getBasePoint(line, column));

					setOfVertices3.put(0, mapMesh.getSurfacePoint(line, column));
					setOfVertices3.put(1, mapMesh.getBasePoint(line, column));
					setOfVertices3.put(2, mapMesh.getBasePoint(line - 1, column));

					setOfVertices4.put(0, mapMesh.getBasePoint(line - 1, column));
					setOfVertices4.put(1, mapMesh.getSurfacePoint(line - 1, column));
					setOfVertices4.put(2, mapMesh.getSurfacePoint(line, column));

					setOfFaces.put(k++, setOfVertices1);
					setOfFaces.put(k++, setOfVertices2);
					setOfFaces.put(k++, setOfVertices3);
					setOfFaces.put(k++, setOfVertices4);
				}

				if (isCenter(line, column, width - 1, height)) {

					TreeMap<Integer, Point3D> setOfVertices1 = new TreeMap<>();
					TreeMap<Integer, Point3D> setOfVertices2 = new TreeMap<>();
					TreeMap<Integer, Point3D> setOfVertices3 = new TreeMap<>();
					TreeMap<Integer, Point3D> setOfVertices4 = new TreeMap<>();

					setOfVertices1.put(0, mapMesh.getSurfacePoint(line, column));
					setOfVertices1.put(1, mapMesh.getSurfacePoint(line - 1, column));
					setOfVertices1.put(2, mapMesh.getSurfacePoint(line, column - 1));

					setOfVertices2.put(0, mapMesh.getSurfacePoint(line, column));
					setOfVertices2.put(1, mapMesh.getSurfacePoint(line - 1, column + 1));
					setOfVertices2.put(2, mapMesh.getSurfacePoint(line - 1, column));

					setOfVertices3.put(0, mapMesh.getBasePoint(line, column - 1));
					setOfVertices3.put(1, mapMesh.getBasePoint(line - 1, column));
					setOfVertices3.put(2, mapMesh.getBasePoint(line, column));

					setOfVertices4.put(0, mapMesh.getBasePoint(line - 1, column));
					setOfVertices4.put(1, mapMesh.getBasePoint(line - 1, column + 1));
					setOfVertices4.put(2, mapMesh.getBasePoint(line, column));

					setOfFaces.put(k++, setOfVertices1);
					setOfFaces.put(k++, setOfVertices2);
					setOfFaces.put(k++, setOfVertices3);
					setOfFaces.put(k++, setOfVertices4);
				}
			}
		}

		Config.Debug("-- Affectation des faces de la map");
		
		k--;
		WB_Triangle wb_triangle[] = new WB_Triangle[k];
		
		System.out.println("k : " +k);

		for (int key = 0; key < k; key++) {
			wb_triangle[key] = new WB_Triangle(setOfFaces.get(key).get(0), setOfFaces.get(key).get(1),
					setOfFaces.get(key).get(2));
		}

		
		Config.Debug("-- Creation d'une HE_Mesh");
		
		HEC_FromTriangles creator = new HEC_FromTriangles();
		creator.setTriangles(wb_triangle);
		HE_Mesh he_mesh= new HE_Mesh(creator);
		
		
		Config.Debug("-- Nettoyage de l'HE_Mesh");
		he_mesh.clean();
		
		Config.Debug("-- Affectation de l'HE_Mesh");
		
		mapMesh.setHe_mesh(he_mesh);
		
		return mapMesh;
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
	public boolean haveToRaised(BufferedImage bufferedImage, double line, double column, double beginWidth,
			double endWidth, double beginHeight, double endHeight) {

		// double begin = bufferedImage.getWidth() * 0.1;
		// double end = bufferedImage.getWidth() * 0.9;

		// boolean condition1, condition2, condition3, condition4, condition5,
		// letter;
		boolean condition1, condition2, condition3, condition4, condition5;

		condition1 = column >= beginWidth && column <= endWidth && line >= beginHeight
				&& line <= (bufferedImage.getHeight() - 1) - beginWidth; // zone
																			// du
																			// restangle
																			// socle

		condition2 = column >= ((bufferedImage.getWidth() - 1) - beginWidth) / 2
				&& column <= ((bufferedImage.getWidth() - 1) + beginWidth) / 2 && line <= beginHeight; // slot
																										// haut

		condition3 = column <= beginWidth && line >= ((bufferedImage.getHeight() - 1) - beginHeight) / 2
				&& line <= ((bufferedImage.getHeight() - 1) + beginHeight) / 2; // slot
																				// gauche

		condition4 = column >= ((bufferedImage.getWidth() - 1) - beginWidth) / 2
				&& column <= ((bufferedImage.getWidth() - 1) + beginWidth) / 2
				&& line >= (bufferedImage.getHeight() - 1) - beginHeight; // slot
																			// bas

		condition5 = line >= ((bufferedImage.getHeight() - 1) - beginHeight) / 2
				&& line <= ((bufferedImage.getHeight() - 1) + beginHeight) / 2 && column >= endWidth; // slot
																										// droit

		// letter = column >= 700.0 && column <= 800.0 && line >= 650.0 && line
		// <= 850.0; // TEMPORAIRE !! - TODO GENERATE A LETTER
		// return (condition1 && !letter) || condition2 || condition3 ||
		// condition4 || condition5;

		return condition1 || condition2 || condition3 || condition4 || condition5;
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

	/**
	 * Method to generate clip
	 * 
	 * @param bufferedImageParcel
	 * @return Clip structure 3___4 11___12 | |5__________7| | | | |
	 *         6__________8 | |___| |___| 1 2 9 10 Vertices s0X: vertices at the
	 *         top of verticeX
	 */
	public MapMesh clipGenerator(BufferedImage bufferedImageParcel) {

		/*
		 * double deb = bufferedImageParcel.getWidth() * 0.1; MapMesh clipMesh =
		 * new MapMesh(bufferedImageParcel.getHeight(),
		 * bufferedImageParcel.getWidth()); Vertices vertices1 = new Vertices(0,
		 * 0, 0); clipMesh.getSetOfVertices().put(vertices1.getId(), vertices1);
		 * Vertices vertices01 = new Vertices(0, 3, 0);
		 * clipMesh.getSetOfVertices().put(vertices01.getId(), vertices01);
		 * Vertices vertices2 = new Vertices(deb / 2, 0, 0);
		 * clipMesh.getSetOfVertices().put(vertices2.getId(), vertices2);
		 * Vertices vertices02 = new Vertices(deb / 2, 3, 0);
		 * clipMesh.getSetOfVertices().put(vertices02.getId(), vertices02);
		 * Vertices vertices3 = new Vertices(0, 0, 2 * deb);
		 * clipMesh.getSetOfVertices().put(vertices3.getId(), vertices3);
		 * Vertices vertices03 = new Vertices(0, 3, 2 * deb);
		 * clipMesh.getSetOfVertices().put(vertices03.getId(), vertices03);
		 * Vertices vertices4 = new Vertices(deb / 2, 0, 2 * deb);
		 * clipMesh.getSetOfVertices().put(vertices4.getId(), vertices4);
		 * Vertices vertices04 = new Vertices(deb / 2, 3, 2 * deb);
		 * clipMesh.getSetOfVertices().put(vertices04.getId(), vertices04);
		 * Vertices vertices5 = new Vertices(deb / 2, 0, 1.5 * deb);
		 * clipMesh.getSetOfVertices().put(vertices5.getId(), vertices5);
		 * Vertices vertices05 = new Vertices(deb / 2, 3, 1.5 * deb);
		 * clipMesh.getSetOfVertices().put(vertices05.getId(), vertices05);
		 * Vertices vertices6 = new Vertices(deb / 2, 0, deb / 2);
		 * clipMesh.getSetOfVertices().put(vertices6.getId(), vertices6);
		 * Vertices vertices06 = new Vertices(deb / 2, 3, deb / 2);
		 * clipMesh.getSetOfVertices().put(vertices06.getId(), vertices06);
		 * Vertices vertices7 = new Vertices(2.5 * deb, 0, 1.5 * deb);
		 * clipMesh.getSetOfVertices().put(vertices7.getId(), vertices7);
		 * Vertices vertices07 = new Vertices(2.5 * deb, 3, 1.5 * deb);
		 * clipMesh.getSetOfVertices().put(vertices07.getId(), vertices07);
		 * Vertices vertices8 = new Vertices(2.5 * deb, 0, deb / 2);
		 * clipMesh.getSetOfVertices().put(vertices8.getId(), vertices8);
		 * Vertices vertices08 = new Vertices(2.5 * deb, 3, deb / 2);
		 * clipMesh.getSetOfVertices().put(vertices08.getId(), vertices08);
		 * Vertices vertices9 = new Vertices(2.5 * deb, 0, 0);
		 * clipMesh.getSetOfVertices().put(vertices9.getId(), vertices9);
		 * Vertices vertices09 = new Vertices(2.5 * deb, 3, 0);
		 * clipMesh.getSetOfVertices().put(vertices09.getId(), vertices09);
		 * Vertices vertices10 = new Vertices(3 * deb, 0, 0);
		 * clipMesh.getSetOfVertices().put(vertices10.getId(), vertices10);
		 * Vertices vertices010 = new Vertices(3 * deb, 3, 0);
		 * clipMesh.getSetOfVertices().put(vertices010.getId(), vertices010);
		 * Vertices vertices11 = new Vertices(2.5 * deb, 0, 2 * deb);
		 * clipMesh.getSetOfVertices().put(vertices11.getId(), vertices11);
		 * Vertices vertices011 = new Vertices(2.5 * deb, 3, 2 * deb);
		 * clipMesh.getSetOfVertices().put(vertices011.getId(), vertices011);
		 * Vertices vertices12 = new Vertices(3 * deb, 0, 2 * deb);
		 * clipMesh.getSetOfVertices().put(vertices12.getId(), vertices12);
		 * Vertices vertices012 = new Vertices(3 * deb, 3, 2 * deb);
		 * clipMesh.getSetOfVertices().put(vertices012.getId(), vertices012);
		 * 
		 * // faces horizontales clipMesh.getSetOfFaces().add(new
		 * Face(vertices1.getId(), vertices2.getId(), vertices3.getId()));
		 * clipMesh.getSetOfFaces().add(new Face(vertices01.getId(),
		 * vertices02.getId(), vertices03.getId()));
		 * clipMesh.getSetOfFaces().add(new Face(vertices2.getId(),
		 * vertices3.getId(), vertices4.getId()));
		 * clipMesh.getSetOfFaces().add(new Face(vertices02.getId(),
		 * vertices03.getId(), vertices04.getId()));
		 * clipMesh.getSetOfFaces().add(new Face(vertices5.getId(),
		 * vertices6.getId(), vertices7.getId()));
		 * clipMesh.getSetOfFaces().add(new Face(vertices05.getId(),
		 * vertices06.getId(), vertices07.getId()));
		 * clipMesh.getSetOfFaces().add(new Face(vertices6.getId(),
		 * vertices7.getId(), vertices8.getId()));
		 * clipMesh.getSetOfFaces().add(new Face(vertices06.getId(),
		 * vertices07.getId(), vertices08.getId()));
		 * clipMesh.getSetOfFaces().add(new Face(vertices9.getId(),
		 * vertices10.getId(), vertices11.getId()));
		 * clipMesh.getSetOfFaces().add(new Face(vertices09.getId(),
		 * vertices010.getId(), vertices011.getId()));
		 * clipMesh.getSetOfFaces().add(new Face(vertices10.getId(),
		 * vertices11.getId(), vertices12.getId()));
		 * clipMesh.getSetOfFaces().add(new Face(vertices010.getId(),
		 * vertices011.getId(), vertices012.getId()));
		 * 
		 * // faces verticales
		 * 
		 * clipMesh.getSetOfFaces().add(new Face(vertices1.getId(),
		 * vertices01.getId(), vertices3.getId()));
		 * clipMesh.getSetOfFaces().add(new Face(vertices3.getId(),
		 * vertices03.getId(), vertices01.getId()));
		 * 
		 * clipMesh.getSetOfFaces().add(new Face(vertices1.getId(),
		 * vertices01.getId(), vertices2.getId()));
		 * clipMesh.getSetOfFaces().add(new Face(vertices01.getId(),
		 * vertices02.getId(), vertices2.getId()));
		 * 
		 * clipMesh.getSetOfFaces().add(new Face(vertices3.getId(),
		 * vertices03.getId(), vertices4.getId()));
		 * clipMesh.getSetOfFaces().add(new Face(vertices03.getId(),
		 * vertices04.getId(), vertices4.getId()));
		 * 
		 * clipMesh.getSetOfFaces().add(new Face(vertices2.getId(),
		 * vertices02.getId(), vertices6.getId()));
		 * clipMesh.getSetOfFaces().add(new Face(vertices02.getId(),
		 * vertices06.getId(), vertices6.getId()));
		 * 
		 * clipMesh.getSetOfFaces().add(new Face(vertices4.getId(),
		 * vertices04.getId(), vertices5.getId()));
		 * clipMesh.getSetOfFaces().add(new Face(vertices04.getId(),
		 * vertices05.getId(), vertices5.getId()));
		 * 
		 * clipMesh.getSetOfFaces().add(new Face(vertices6.getId(),
		 * vertices06.getId(), vertices8.getId()));
		 * clipMesh.getSetOfFaces().add(new Face(vertices06.getId(),
		 * vertices08.getId(), vertices8.getId()));
		 * 
		 * clipMesh.getSetOfFaces().add(new Face(vertices5.getId(),
		 * vertices05.getId(), vertices7.getId()));
		 * clipMesh.getSetOfFaces().add(new Face(vertices05.getId(),
		 * vertices07.getId(), vertices7.getId()));
		 * 
		 * clipMesh.getSetOfFaces().add(new Face(vertices8.getId(),
		 * vertices9.getId(), vertices08.getId()));
		 * clipMesh.getSetOfFaces().add(new Face(vertices08.getId(),
		 * vertices9.getId(), vertices09.getId()));
		 * 
		 * clipMesh.getSetOfFaces().add(new Face(vertices7.getId(),
		 * vertices07.getId(), vertices11.getId()));
		 * clipMesh.getSetOfFaces().add(new Face(vertices07.getId(),
		 * vertices11.getId(), vertices011.getId()));
		 * 
		 * clipMesh.getSetOfFaces().add(new Face(vertices9.getId(),
		 * vertices10.getId(), vertices09.getId()));
		 * clipMesh.getSetOfFaces().add(new Face(vertices09.getId(),
		 * vertices10.getId(), vertices010.getId()));
		 * 
		 * clipMesh.getSetOfFaces().add(new Face(vertices11.getId(),
		 * vertices12.getId(), vertices011.getId()));
		 * clipMesh.getSetOfFaces().add(new Face(vertices011.getId(),
		 * vertices012.getId(), vertices12.getId()));
		 * 
		 * clipMesh.getSetOfFaces().add(new Face(vertices12.getId(),
		 * vertices10.getId(), vertices012.getId()));
		 * clipMesh.getSetOfFaces().add(new Face(vertices10.getId(),
		 * vertices012.getId(), vertices010.getId()));
		 * 
		 * return clipMesh;
		 */
		return null;
	}

}