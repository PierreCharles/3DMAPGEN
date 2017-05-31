package model.treatment;

import java.awt.image.BufferedImage;
import java.util.TreeMap;

import config.Config;
import model.Parameter;
import model.mesh.Face;
import model.mesh.Mesh;
import model.mesh.Vertices;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Class treatment It is the class for execute the treatment of the task
 * 
 * @author
 *
 */
public class Treatment {
	
	private Parameter parameters;
	private ImageLoader imageLoader;
	private int heightCutNumber, widthCutNumber, heightOfPartel, widthOfParcel;
		
	public int getHeightCutNumber() {
		return heightCutNumber;
	}


	public int getWidthCutNumber() {
		return widthCutNumber;
	}


	public int getHeightOfPartel() {
		return heightOfPartel;
	}


	public int getWidthOfParcel() {
		return widthOfParcel;
	}


	public Treatment(Parameter parameters, ImageLoader imageLoader) {
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
	public List<Mesh> executeTreatment()
	{
		List<Mesh> parcelsList = new ArrayList<>();
		List<BufferedImage> imagesList = cutImage();

		imagesList.forEach((image) -> {
			parcelsList.add(parcelToMesh(image, parameters));
		});
		
		parcelsList.forEach((parcelle) -> {
			if(Config.DEBUG){
				System.out.println("Mise à l'échelle parcelle");
			}
			scalling(parcelle, imagesList.get(0), parameters);
		});
		return parcelsList;
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
		heightCutNumber = (int) Math.ceil(parameters.getImageHeight() / (parameters.getMaxHeightOfPrint()) / 10);
		widthCutNumber = (int) Math.ceil(parameters.getImageWidth() / (parameters.getMaxWidthOfPrint() / 10));
		heightOfPartel = (int) Math.floor(imageBase.getHeight() / heightCutNumber);
		widthOfParcel = (int) Math.floor(imageBase.getWidth() / widthCutNumber);

		if (Config.DEBUG) {
			System.out.println("Hauteur de la partelle : " + heightOfPartel);
			System.out.println("Largeur de la partelle : " + widthOfParcel);
		}

		for (int x = 0; x < widthCutNumber; x++) {
			for (int y = 0; y < heightCutNumber; y++) {
				imageList.add(imageBase.getSubimage(x * widthOfParcel, y * heightOfPartel, widthOfParcel,
						heightOfPartel));
			}
		}
		return imageList;
	}
	

	/**
	 * Allow to obtain the height of a pixel of the loaded image in function of
	 * these coordonate
	 * 
	 * @param line y coordonate
	 * @param column x coordonate
	 * @param resolution Resolution of the height in function of the grey level
	 * @param bufferedImage loaded image into application
	 * @return the attempt height for the vertices of the mesh associated at the attempt pixel
	 */
	public double getPixelHeight(double line, double column, double resolution) {
		int pixel = imageLoader.getBufferedImage().getRGB((int) Math.floor(column), (int) Math.floor(line));
		int red = (pixel >> 16) & 0xff;
		int green = (pixel >> 8) & 0xff;
		int blue = (pixel) & 0xff;
		int medium = 255 - (red + green + blue) / 3;
		return (resolution * medium) + 5;
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
	public Mesh parcelToMesh(BufferedImage bufferedImage, Parameter parameter) {

		Mesh mesh = new Mesh();
		double resolution = parameters.getMeshHeight() / 256;
		double height = bufferedImage.getHeight() - 1;
		double width = bufferedImage.getWidth() - 1;
		double tickness = 3;
		double beginWidth = 0.1 * width, endWidth = 0.9 * width, beginHeight = 0.1 * height, endHieght = 0.9 * height;

		for (double line = 0; line < height; line++) {
			for (double column = 0; column < width; column++) {
				// Create a surface coordonates points  : line;column
				mesh.addVertices(line, column,
						new Vertices(line, getPixelHeight(line, column, resolution), column));
			}
		}

		for (double line = 0; line < height; line++) {
			for (double column = 0; column < width; column++) {
				if (haveToRaised(bufferedImage, line, column, beginWidth, endWidth, beginHeight, endHieght)) {
					mesh.addVerticesBase(line, column, new Vertices(line, tickness, column));
				} else {
					// Create a point coordonate base : ligne;colonne
					mesh.addVerticesBase(line, column, new Vertices(line, 0, column));
				}
			}
		}

		for (double line = 0; line < height; line++) {
			for (double column = 0; column < width; column++) {

				if (isTopEdge(line, column)) {
					// Création du cotÃ© haut ou bas 
					mesh.addFace(new Face(mesh.getSurfacePoint(line, column).getId(),
							mesh.getBasePoint(line, column).getId(),mesh.getBasePoint(line, column - 1).getId()));
					mesh.addFace(new Face(mesh.getBasePoint(line, column - 1).getId(),
							mesh.getSurfacePoint(line, column - 1).getId(),
							mesh.getSurfacePoint(line, column).getId()));
				}
				
				if (isBottomEdge(line, column, width, height - 1)) {
					// Création du cotÃ© haut ou bas 
					mesh.addFace(new Face(mesh.getBasePoint(line, column - 1).getId(),
							mesh.getBasePoint(line, column).getId(),mesh.getSurfacePoint(line, column).getId() ));
					mesh.addFace(new Face(mesh.getSurfacePoint(line, column).getId(),
							mesh.getSurfacePoint(line, column - 1).getId(),
							mesh.getBasePoint(line, column - 1).getId()));
				}
				
				if (isLeftEdge(line, column)) {
					// Création de la face de la surface collée au bord
					mesh.addFace(new Face(mesh.getSurfacePoint(line, column).getId(),
							mesh.getSurfacePoint(line - 1, column + 1).getId(),
							mesh.getSurfacePoint(line - 1, column).getId()));
					// Création de la face du socle collée au bord
					mesh.addFace(new Face(mesh.getBasePoint(line - 1, column).getId(),
							mesh.getBasePoint(line - 1, column + 1).getId(),
							mesh.getBasePoint(line, column).getId()));
					// Création du coté gauche 
					mesh.addFace(new Face(mesh.getSurfacePoint(line, column).getId(),
							mesh.getSurfacePoint(line - 1, column).getId(), mesh.getBasePoint(line, column).getId()));
					mesh.addFace(new Face(mesh.getSurfacePoint(line - 1, column).getId(),
							mesh.getBasePoint(line - 1, column).getId(), mesh.getBasePoint(line, column).getId()));
				}

				if (isRightEdge(line, column, width - 1, height)) {
					// Création de la face de la surface collée au bord */
					mesh.addFace(new Face(mesh.getSurfacePoint(line, column).getId(),
							mesh.getSurfacePoint(line - 1, column).getId(),
							mesh.getSurfacePoint(line, column - 1).getId()));
					// Création de la face du socle collé au bord 
					mesh.addFace(new Face(mesh.getBasePoint(line, column - 1).getId(),
							mesh.getBasePoint(line - 1, column).getId(), mesh.getBasePoint(line, column).getId()));
					// Création du coté droit 
					mesh.addFace(new Face(mesh.getSurfacePoint(line, column).getId(),
							mesh.getBasePoint(line, column).getId(), mesh.getBasePoint(line - 1, column).getId()));
					mesh.addFace(new Face(mesh.getBasePoint(line - 1, column).getId(),
							mesh.getSurfacePoint(line - 1, column).getId(),
							mesh.getSurfacePoint(line, column).getId()));
				}
			
				if (isCenter(line, column, width - 1, height)) {
					mesh.addFace(new Face(mesh.getSurfacePoint(line, column).getId(),
							mesh.getSurfacePoint(line - 1, column).getId(),
							mesh.getSurfacePoint(line, column - 1).getId()));
					mesh.addFace(new Face(mesh.getSurfacePoint(line, column).getId(),
							mesh.getSurfacePoint(line - 1, column + 1).getId(),
							mesh.getSurfacePoint(line - 1, column).getId()));

					mesh.addFace(new Face(mesh.getBasePoint(line, column - 1).getId(),
							mesh.getBasePoint(line - 1, column).getId(), mesh.getBasePoint(line, column).getId()));
					mesh.addFace(new Face(mesh.getBasePoint(line - 1, column).getId(),
							mesh.getBasePoint(line - 1, column + 1).getId(),
							mesh.getBasePoint(line, column).getId()));
				}			
			}
		}
		return mesh;
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
		double begin = bufferedImage.getWidth() * 0.1;
		double end = bufferedImage.getWidth() * 0.9;
		
		boolean condition1, condition2, condition3, condition4, condition5, letter;
		
		condition1 = column >= beginWidth && column <= endWidth && line >= beginHeight
				&& line <= (bufferedImage.getHeight() - 1) - beginWidth; // zone  du restangle socle
		
		condition2 = column >= ((bufferedImage.getWidth() - 1) - beginWidth) / 2
				&& column <= ((bufferedImage.getWidth() - 1) + beginWidth) / 2 && line <= beginHeight; // slot haut
		
		condition3 = column <= beginWidth && line >= ((bufferedImage.getHeight() - 1) - beginHeight) / 2
				&& line <= ((bufferedImage.getHeight() - 1) + beginHeight) / 2; // slot gauche		
				
		condition4 = column >= ((bufferedImage.getWidth() - 1) - beginWidth) / 2
				&& column <= ((bufferedImage.getWidth() - 1) + beginWidth) / 2
				&& line >= (bufferedImage.getHeight() - 1) - beginHeight; // slot bas
		
		condition5 = line >= ((bufferedImage.getHeight() - 1) - beginHeight) / 2
				&& line <= ((bufferedImage.getHeight() - 1) + beginHeight) / 2 && column >= endWidth ; // slot droit
				
		// TEMPORAIRE !! - TODO GENERATE A LETTER				
		letter = column >= 700.0 && column <= 800.0 && line >= 650.0 && line <=850.0;

		return (condition1 && !letter) || condition2 || condition3 || condition4 || condition5;
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
     * @param bufferedImageParcel
     * @return
     * Clip structure
     * 3___4           11___12
     * |   |5__________7|   |
     * |                    |
     * |    6__________8    |
     * |___|            |___|
     * 1   2           9    10
     * Vertices s0X: vertices at the top of verticeX
     */
	public Mesh clipGenerator(BufferedImage bufferedImageParcel) {
		Mesh clipMesh = new Mesh();
		double deb = bufferedImageParcel.getWidth() * 0.1;
		Vertices vertices1 = new Vertices(0, 0, 0);
		clipMesh.getSetOfVertices().put(vertices1.getId(), vertices1);
		Vertices vertices01 = new Vertices(0, 3, 0);
		clipMesh.getSetOfVertices().put(vertices01.getId(), vertices01);
		Vertices vertices2 = new Vertices(deb / 2, 0, 0);
		clipMesh.getSetOfVertices().put(vertices2.getId(), vertices2);
		Vertices vertices02 = new Vertices(deb / 2, 3, 0);
		clipMesh.getSetOfVertices().put(vertices02.getId(), vertices02);
		Vertices vertices3 = new Vertices(0, 0, 2 * deb);
		clipMesh.getSetOfVertices().put(vertices3.getId(), vertices3);
		Vertices vertices03 = new Vertices(0, 3, 2 * deb);
		clipMesh.getSetOfVertices().put(vertices03.getId(), vertices03);
		Vertices vertices4 = new Vertices(deb / 2, 0, 2 * deb);
		clipMesh.getSetOfVertices().put(vertices4.getId(), vertices4);
		Vertices vertices04 = new Vertices(deb / 2, 3, 2 * deb);
		clipMesh.getSetOfVertices().put(vertices04.getId(), vertices04);
		Vertices vertices5 = new Vertices(deb / 2, 0, 1.5 * deb);
		clipMesh.getSetOfVertices().put(vertices5.getId(), vertices5);
		Vertices vertices05 = new Vertices(deb / 2, 3, 1.5 * deb);
		clipMesh.getSetOfVertices().put(vertices05.getId(), vertices05);
		Vertices vertices6 = new Vertices(deb / 2, 0, deb / 2);
		clipMesh.getSetOfVertices().put(vertices6.getId(), vertices6);
		Vertices vertices06 = new Vertices(deb / 2, 3, deb / 2);
		clipMesh.getSetOfVertices().put(vertices06.getId(), vertices06);
		Vertices vertices7 = new Vertices(2.5 * deb, 0, 1.5 * deb);
		clipMesh.getSetOfVertices().put(vertices7.getId(), vertices7);
		Vertices vertices07 = new Vertices(2.5 * deb, 3, 1.5 * deb);
		clipMesh.getSetOfVertices().put(vertices07.getId(), vertices07);
		Vertices vertices8 = new Vertices(2.5 * deb, 0, deb / 2);
		clipMesh.getSetOfVertices().put(vertices8.getId(), vertices8);
		Vertices vertices08 = new Vertices(2.5 * deb, 3, deb / 2);
		clipMesh.getSetOfVertices().put(vertices08.getId(), vertices08);
		Vertices vertices9 = new Vertices(2.5 * deb, 0, 0);
		clipMesh.getSetOfVertices().put(vertices9.getId(), vertices9);
		Vertices vertices09 = new Vertices(2.5 * deb, 3, 0);
		clipMesh.getSetOfVertices().put(vertices09.getId(), vertices09);
		Vertices vertices10 = new Vertices(3 * deb, 0, 0);
		clipMesh.getSetOfVertices().put(vertices10.getId(), vertices10);
		Vertices vertices010 = new Vertices(3 * deb, 3, 0);
		clipMesh.getSetOfVertices().put(vertices010.getId(), vertices010);
		Vertices vertices11 = new Vertices(2.5 * deb, 0, 2 * deb);
		clipMesh.getSetOfVertices().put(vertices11.getId(), vertices11);
		Vertices vertices011 = new Vertices(2.5 * deb, 3, 2 * deb);
		clipMesh.getSetOfVertices().put(vertices011.getId(), vertices011);
		Vertices vertices12 = new Vertices(3 * deb, 0, 2 * deb);
		clipMesh.getSetOfVertices().put(vertices12.getId(), vertices12);
		Vertices vertices012 = new Vertices(3 * deb, 3, 2 * deb);
		clipMesh.getSetOfVertices().put(vertices012.getId(), vertices012);

		// faces horizontales
		clipMesh.getSetOfFaces().add(new Face(vertices1.getId(), vertices2.getId(), vertices3.getId()));
		clipMesh.getSetOfFaces().add(new Face(vertices01.getId(), vertices02.getId(), vertices03.getId()));
		clipMesh.getSetOfFaces().add(new Face(vertices2.getId(), vertices3.getId(), vertices4.getId()));
		clipMesh.getSetOfFaces().add(new Face(vertices02.getId(), vertices03.getId(), vertices04.getId()));
		clipMesh.getSetOfFaces().add(new Face(vertices5.getId(), vertices6.getId(), vertices7.getId()));
		clipMesh.getSetOfFaces().add(new Face(vertices05.getId(), vertices06.getId(), vertices07.getId()));
		clipMesh.getSetOfFaces().add(new Face(vertices6.getId(), vertices7.getId(), vertices8.getId()));
		clipMesh.getSetOfFaces().add(new Face(vertices06.getId(), vertices07.getId(), vertices08.getId()));
		clipMesh.getSetOfFaces().add(new Face(vertices9.getId(), vertices10.getId(), vertices11.getId()));
		clipMesh.getSetOfFaces().add(new Face(vertices09.getId(), vertices010.getId(), vertices011.getId()));
		clipMesh.getSetOfFaces().add(new Face(vertices10.getId(), vertices11.getId(), vertices12.getId()));
		clipMesh.getSetOfFaces().add(new Face(vertices010.getId(), vertices011.getId(), vertices012.getId()));

		// faces verticales

		clipMesh.getSetOfFaces().add(new Face(vertices1.getId(), vertices01.getId(), vertices3.getId()));
		clipMesh.getSetOfFaces().add(new Face(vertices3.getId(), vertices03.getId(), vertices01.getId()));

		clipMesh.getSetOfFaces().add(new Face(vertices1.getId(), vertices01.getId(), vertices2.getId()));
		clipMesh.getSetOfFaces().add(new Face(vertices01.getId(), vertices02.getId(), vertices2.getId()));

		clipMesh.getSetOfFaces().add(new Face(vertices3.getId(), vertices03.getId(), vertices4.getId()));
		clipMesh.getSetOfFaces().add(new Face(vertices03.getId(), vertices04.getId(), vertices4.getId()));

		clipMesh.getSetOfFaces().add(new Face(vertices2.getId(), vertices02.getId(), vertices6.getId()));
		clipMesh.getSetOfFaces().add(new Face(vertices02.getId(), vertices06.getId(), vertices6.getId()));

		clipMesh.getSetOfFaces().add(new Face(vertices4.getId(), vertices04.getId(), vertices5.getId()));
		clipMesh.getSetOfFaces().add(new Face(vertices04.getId(), vertices05.getId(), vertices5.getId()));

		clipMesh.getSetOfFaces().add(new Face(vertices6.getId(), vertices06.getId(), vertices8.getId()));
		clipMesh.getSetOfFaces().add(new Face(vertices06.getId(), vertices08.getId(), vertices8.getId()));

		clipMesh.getSetOfFaces().add(new Face(vertices5.getId(), vertices05.getId(), vertices7.getId()));
		clipMesh.getSetOfFaces().add(new Face(vertices05.getId(), vertices07.getId(), vertices7.getId()));

		clipMesh.getSetOfFaces().add(new Face(vertices8.getId(), vertices9.getId(), vertices08.getId()));
		clipMesh.getSetOfFaces().add(new Face(vertices08.getId(), vertices9.getId(), vertices09.getId()));

		clipMesh.getSetOfFaces().add(new Face(vertices7.getId(), vertices07.getId(), vertices11.getId()));
		clipMesh.getSetOfFaces().add(new Face(vertices07.getId(), vertices11.getId(), vertices011.getId()));

		clipMesh.getSetOfFaces().add(new Face(vertices9.getId(), vertices10.getId(), vertices09.getId()));
		clipMesh.getSetOfFaces().add(new Face(vertices09.getId(), vertices10.getId(), vertices010.getId()));

		clipMesh.getSetOfFaces().add(new Face(vertices11.getId(), vertices12.getId(), vertices011.getId()));
		clipMesh.getSetOfFaces().add(new Face(vertices011.getId(), vertices012.getId(), vertices12.getId()));

		clipMesh.getSetOfFaces().add(new Face(vertices12.getId(), vertices10.getId(), vertices012.getId()));
		clipMesh.getSetOfFaces().add(new Face(vertices10.getId(), vertices012.getId(), vertices010.getId()));

		return clipMesh;
	}

	/**
	 * Method for scalling
	 * 
	 * @param mesh
	 * @param bufferedImage
	 * @param parameter
	 */
	public void scalling(Mesh mesh, BufferedImage bufferedImage, Parameter parameter) {

		double ratioX = parameter.getMaxWidthOfPrint() / widthOfParcel;
		double ratioZ = parameter.getMaxHeightOfPrint() / heightOfPartel;

		Set<Map.Entry<Double, TreeMap>> setLine = mesh.getSetOfVertices().entrySet();
		Iterator<Map.Entry<Double, TreeMap>> iterator = setLine.iterator();
		while (iterator.hasNext()) {
			Map.Entry<Double, TreeMap> entry = iterator.next();
			TreeMap verticesTreeMap = entry.getValue();

			Set<Map.Entry<Double, Vertices>> setColumn = verticesTreeMap.entrySet();
			Iterator<Map.Entry<Double, Vertices>> iterator2 = setColumn.iterator();

			while (iterator2.hasNext()) {
				Map.Entry<Double, Vertices> verticesEntry = iterator2.next();
				verticesEntry.getValue().setX(verticesEntry.getValue().getX() * ratioX);
				verticesEntry.getValue().setZ(verticesEntry.getValue().getZ() * ratioZ);
			}
		}

		// Ecriture de l'ensemble des points du socle
		Set<Map.Entry<Double, TreeMap>> setBaseLine = mesh.getSetOfVerticesBase().entrySet();
		Iterator<Map.Entry<Double, TreeMap>> iterator3 = setBaseLine.iterator();
		while (iterator3.hasNext()) {
			Map.Entry<Double, TreeMap> entry2 = iterator3.next();
			TreeMap verticesTreeMapBase = entry2.getValue();

			Set<Map.Entry<Double, Vertices>> setBaseColum = verticesTreeMapBase.entrySet();
			Iterator<Map.Entry<Double, Vertices>> iterator4 = setBaseColum.iterator();

			while (iterator4.hasNext()) {
				Map.Entry<Double, Vertices> verticesEntryBase = iterator4.next();
				verticesEntryBase.getValue().setX(verticesEntryBase.getValue().getX() * ratioX);
				verticesEntryBase.getValue().setZ(verticesEntryBase.getValue().getZ() * ratioZ);
			}
		}
	}

}