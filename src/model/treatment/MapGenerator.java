package model.treatment;

import java.awt.image.BufferedImage;

import config.Config;
import model.ImageLoader;
import model.Parameter;
import model.mesh.Point3D;
import model.mesh.MapMesh;
import model.mesh.ObjectMesh;
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
	
	// Add the ID of base face to raised
	ArrayList<Integer> baseFacesList = new ArrayList<Integer>(
			Arrays.asList(5, 15, 16, 17, 55, 45, 56, 67, 53, 64, 65, 75, 103, 104, 105, 115, 36, 37, 38, 39, 40, 47,
					48, 49, 50, 51, 58, 59, 60, 61, 62, 69, 70, 71, 72, 73, 80, 81, 82, 83, 84));;

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
	public List<ObjectMesh> executeTreatment() {

		List<ObjectMesh> parcelList = new ArrayList<>();
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

		Config.Debug(imageList.size() + " parcelle(s) de " + heightOfParcel + " par : " + widthOfParcel);

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
	public double getPixelHeight(BufferedImage bufferedImage, int line, int column, double resolution) {
		int pixel = bufferedImage.getRGB(line, column);
		int red = (pixel >> 16) & 0xff;
		int green = (pixel >> 8) & 0xff;
		int blue = (pixel) & 0xff;
		int medium = 255 - (red + green + blue) / 3;
		return (resolution * medium) + Config.MAP_ELEVATION;
	}

	/**
	 * Methode to know if a point is on a border of the map
	 * 
	 * @return Boolean : true if is a border, else return false
	 */
	private boolean pointIsBorder(int line, int column, int height, int width) {
		// Top || Left || Bottom || Right
		return (line == 0) || (column == 0) || (line == height - 1) || (column == width - 1);
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
		
		int height = bufferedImage.getHeight();
		int width = bufferedImage.getWidth();
		double resolution = parameters.getMeshHeight() / 256;
		double ratioX = parameters.getMaxHeightOfPrint() / (heightOfParcel-1);
		double ratioZ = parameters.getMaxWidthOfPrint() / (widthOfParcel-1);
		double realHeight = (height-1)*ratioX;
		double realWidth = (height-1)*ratioX;
		double[] basePointTableX = generateBasePointTable(realHeight);
		double[] basePointTableZ = generateBasePointTable(realWidth);
		
		MapMesh mapMesh = new MapMesh();
		Config.Debug("Création : " + mapMesh.getName() + " -> H: " + realHeight + " W: " + (height-1)*ratioX);

		ArrayList<WB_Polygon> wbPolygonList = new ArrayList<WB_Polygon>();

		// Create a surface and base coordinates points : line;column
		createSurfacePoints(mapMesh, height, width, ratioX, ratioZ, bufferedImage, resolution, basePointTableX, basePointTableZ);

		// Creation of the surface faces
		createSurfaceFaces(height, width, wbPolygonList, mapMesh);
		
		// Creation of border map faces
		borderMapFacesCreator(mapMesh, wbPolygonList, basePointTableX, basePointTableZ, height, width);

		//Creation of under map faces (Inner border and under plane surface)
		underMapFacesCreator(mapMesh, wbPolygonList, basePointTableX, basePointTableZ);

		HE_Mesh he_mesh = new HE_Mesh(new HEC_FromPolygons(wbPolygonList));
		mapMesh.setHe_mesh(he_mesh);
		
		return mapMesh;
	}
	
	/**
	 * Methode used to create and index all point of the map into MapMesh TreeMap
	 * 
	 * @param mapMesh
	 * @param height
	 * @param width
	 * @param ratioX
	 * @param ratioZ
	 * @param bufferedImage
	 * @param resolution
	 * @param basePointTableX
	 * @param basePointTableY
	 */
	public void createSurfacePoints(MapMesh mapMesh,int height,int width, double ratioX, double ratioZ, 
			BufferedImage bufferedImage, double resolution, double[] basePointTableX, double[] basePointTableY){

		
		Config.Debug("-- Indexation des points en surface de la map");
		System.out.println("height : "+height + " width : "+width);
		for (int line = 0; line < height; line++) {
			
			for (int column = 0; column < width; column++) {
				
				double linePoint = line * ratioX;
				double columnPoint = column * ratioZ;
				
				//System.out.println("line : "+line + " linePoint : "+ linePoint + "column : "+column + " columnPoint : "+ columnPoint);
				
				mapMesh.addSurfacePoint(line, column, new Point3D(linePoint,
						getPixelHeight(bufferedImage, line, column, resolution), columnPoint));

				if (pointIsBorder(line, column, height, width)) {
					mapMesh.addBaseRaisedSidePoint(line, column,
							new Point3D(linePoint, Config.BASE_MAP_RAISED_SIDE_TICKNESS, columnPoint));
				}
			}
		}
		Config.Debug("-- Indexation des points en dessous de la map");
		for (int a = 0; a < basePointTableX.length; a++) {
			for (int b = 0; b < basePointTableY.length; b++) {
				mapMesh.addBaseRaisedPoint(a, b,
						new Point3D(basePointTableX[a], Config.BASE_MAP_RAISED_TICKNESS, basePointTableY[b]));
				mapMesh.addBasePoint(a, b,
						new Point3D(basePointTableX[a], Config.BASE_MAP_TICKNESS, basePointTableY[b]));
			}
		}
	}


	/**
	 * Methode used to create all surface polygon faces map
	 * @param height
	 * @param width
	 * @param wbPolygonList
	 * @param mapMesh
	 */
	public void createSurfaceFaces(int height, int width,ArrayList<WB_Polygon> wbPolygonList, MapMesh mapMesh){
		
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
	}
	

	
	
	/**
	 * Methode used to generate the border map polygon faces
	 * 
	 * @param mapMesh
	 * @param wbPolygonList
	 * @param basePointTableX
	 * @param basePointTableY
	 * @param height
	 * @param width
	 */
	public void borderMapFacesCreator(MapMesh mapMesh, ArrayList<WB_Polygon> wbPolygonList, double[] basePointTableX, double[] basePointTableY, int height, int width){
		
		Config.Debug("-- Creation des faces sur le coté de la map");
		
		int lengthX = basePointTableX.length - 1;
		int lenghtY = basePointTableY.length - 1;
		
		ArrayList<Point3D> listBottomSidePoint = new ArrayList<Point3D>();
		ArrayList<Point3D> listTopSidePoint = new ArrayList<Point3D>();
		ArrayList<Point3D> listLeftSidePoint = new ArrayList<Point3D>();
		ArrayList<Point3D> listRightSidePoint = new ArrayList<Point3D>();
		
		ArrayList<Point3D> listLeftSideBasePoint1 = new ArrayList<Point3D>();
		ArrayList<Point3D> listLeftSideBasePoint1Tmp = new ArrayList<Point3D>();
		ArrayList<Point3D> listLeftSideBasePoint2 = new ArrayList<Point3D>();
		ArrayList<Point3D> listLeftSideBasePoint2Tmp = new ArrayList<Point3D>();
	
		ArrayList<Point3D> listRightSideBasePoint1 = new ArrayList<Point3D>();
		ArrayList<Point3D> listRightSideBasePoint1Tmp = new ArrayList<Point3D>();
		ArrayList<Point3D> listRightSideBasePoint2 = new ArrayList<Point3D>();
		ArrayList<Point3D> listRightSideBasePoint2Tmp = new ArrayList<Point3D>();
		
		ArrayList<Point3D> listTopSideBasePoint1 = new ArrayList<Point3D>();
		ArrayList<Point3D> listTopSideBasePoint1Tmp = new ArrayList<Point3D>();
		ArrayList<Point3D> listTopSideBasePoint2 = new ArrayList<Point3D>();
		ArrayList<Point3D> listTopSideBasePoint2Tmp = new ArrayList<Point3D>();
		
		ArrayList<Point3D> listBottomSideBasePoint1 = new ArrayList<Point3D>();
		ArrayList<Point3D> listBottomSideBasePoint1Tmp = new ArrayList<Point3D>();
		ArrayList<Point3D> listBottomSideBasePoint2 = new ArrayList<Point3D>();
		ArrayList<Point3D> listBottomSideBasePoint2Tmp = new ArrayList<Point3D>();

		
		for(int a = 0 ; a < basePointTableX.length ; a++){
			if(a<6){
				listBottomSideBasePoint1.add(mapMesh.getBaseRaisedPoint(a, 0));
				listBottomSideBasePoint1Tmp.add(mapMesh.getBasePoint(5-a,0));
				listTopSideBasePoint1.add(mapMesh.getBasePoint(a, lenghtY));
				listTopSideBasePoint1Tmp.add(mapMesh.getBaseRaisedPoint(5-a, lenghtY));
			}
			else{
				listBottomSideBasePoint2.add(mapMesh.getBaseRaisedPoint(a, 0));
				listBottomSideBasePoint2Tmp.add(mapMesh.getBasePoint(lengthX-(a-6),0));
				listTopSideBasePoint2.add(mapMesh.getBasePoint(a, lenghtY));
				listTopSideBasePoint2Tmp.add(mapMesh.getBaseRaisedPoint(lengthX-(a-6), lenghtY));
			}
			listBottomSidePoint.add(mapMesh.getBaseRaisedPoint(lengthX-a, 0));
			listTopSidePoint.add(mapMesh.getBaseRaisedPoint(a,lenghtY));
		}

		for (double line = 0; line < height - 1; line++) {
			// Bottom
			listBottomSidePoint.add(mapMesh.getBaseRaisedSidePoint(line,0));
			wbPolygonList.add(new WB_Polygon(
					mapMesh.getSurfacePoint(line + 1, 0), 
					mapMesh.getBaseRaisedSidePoint(line + 1, 0),
					mapMesh.getBaseRaisedSidePoint(line, 0), 
					mapMesh.getSurfacePoint(line, 0)));
			// Top
			listTopSidePoint.add(mapMesh.getBaseRaisedSidePoint((height-1)-line, width-1));
			wbPolygonList.add(new WB_Polygon(
					mapMesh.getSurfacePoint(line, width - 1), 
					mapMesh.getBaseRaisedSidePoint(line, width - 1),
					mapMesh.getBaseRaisedSidePoint(line + 1, width - 1), 
					mapMesh.getSurfacePoint(line + 1, width - 1)));
		}
		
		
		for(int b = 0 ; b < basePointTableY.length ; b++){	
			if(b<6){
				listLeftSideBasePoint1.add(mapMesh.getBasePoint(0, b));
				listLeftSideBasePoint1Tmp.add(mapMesh.getBaseRaisedPoint(0,5-b));
				listRightSideBasePoint1.add(mapMesh.getBaseRaisedPoint(lengthX, b));
				listRightSideBasePoint1Tmp.add(mapMesh.getBasePoint(lengthX, 5-b));
			}
			else{
				listLeftSideBasePoint2.add(mapMesh.getBasePoint(0, b));
				listLeftSideBasePoint2Tmp.add(mapMesh.getBaseRaisedPoint(0,lenghtY-(b-6)));
				listRightSideBasePoint2.add(mapMesh.getBaseRaisedPoint(lengthX,b));
				listRightSideBasePoint2Tmp.add(mapMesh.getBasePoint(lengthX,lenghtY-(b-6)));
			}
					
			listLeftSidePoint.add(mapMesh.getBaseRaisedPoint(0, b));
			listRightSidePoint.add(mapMesh.getBaseRaisedPoint(lengthX,lenghtY-b));
		}

		for (double column = 0; column < width - 1; column++) {
			
			// Left
			listLeftSidePoint.add(mapMesh.getBaseRaisedSidePoint(0,(width - 1 ) - column));
			wbPolygonList.add(new WB_Polygon(
					mapMesh.getSurfacePoint(0, column), 
					mapMesh.getBaseRaisedSidePoint(0, column),
					mapMesh.getBaseRaisedSidePoint(0, column + 1), 
					mapMesh.getSurfacePoint(0, column + 1)));
			// Right
			listRightSidePoint.add(mapMesh.getBaseRaisedSidePoint(height - 1,column));
			wbPolygonList.add(new WB_Polygon(
					mapMesh.getSurfacePoint(height - 1, column + 1), 
					mapMesh.getBaseRaisedSidePoint(height - 1, column + 1),
					mapMesh.getBaseRaisedSidePoint(height - 1, column), 
					mapMesh.getSurfacePoint(height - 1, column)));
		}
		
		listLeftSidePoint.add(mapMesh.getBaseRaisedSidePoint(0, 0));
		listRightSidePoint.add(mapMesh.getBaseRaisedSidePoint(height - 1,width - 1));
		listBottomSidePoint.add(mapMesh.getBaseRaisedSidePoint(height - 1, 0));
		listTopSidePoint.add(mapMesh.getBaseRaisedSidePoint(0 ,width - 1));
		
		listLeftSideBasePoint1.addAll(listLeftSideBasePoint1Tmp);
		listLeftSideBasePoint2.addAll(listLeftSideBasePoint2Tmp);

		listBottomSideBasePoint1.addAll(listBottomSideBasePoint1Tmp);
		listBottomSideBasePoint2.addAll(listBottomSideBasePoint2Tmp);
		
		listTopSideBasePoint1.addAll(listTopSideBasePoint1Tmp);
		listTopSideBasePoint2.addAll(listTopSideBasePoint2Tmp);
		
		listRightSideBasePoint1.addAll(listRightSideBasePoint1Tmp);
		listRightSideBasePoint2.addAll(listRightSideBasePoint2Tmp);

		wbPolygonList.add(new WB_Polygon(listLeftSidePoint));
		wbPolygonList.add(new WB_Polygon(listRightSidePoint));
		wbPolygonList.add(new WB_Polygon(listBottomSidePoint));
		wbPolygonList.add(new WB_Polygon(listTopSidePoint));
		
		wbPolygonList.add(new WB_Polygon(listLeftSideBasePoint2));
		wbPolygonList.add(new WB_Polygon(listLeftSideBasePoint1));
		
	    wbPolygonList.add(new WB_Polygon(listBottomSideBasePoint2));
		wbPolygonList.add(new WB_Polygon(listBottomSideBasePoint1));
	
	    wbPolygonList.add(new WB_Polygon(listTopSideBasePoint2));
		wbPolygonList.add(new WB_Polygon(listTopSideBasePoint1));
		
	    wbPolygonList.add(new WB_Polygon(listRightSideBasePoint2));
		wbPolygonList.add(new WB_Polygon(listRightSideBasePoint1));
	}
	
	/**
	 * This methode create the under map polygon faces with the basePointTable structure
	 * 
	 * @param mapMesh
	 * @param wbPolygonList
	 * @param basePointTableX
	 * @param basePointTableY
	 */
	public void underMapFacesCreator(MapMesh mapMesh, ArrayList<WB_Polygon> wbPolygonList, double[] basePointTableX, double[] basePointTableY){
		
		Config.Debug("-- Creation des faces sous la map");
		
		int lengthX = basePointTableX.length - 1;
		int lenghtY = basePointTableY.length - 1;

		int idCurrentFace = 0;
		for (int a = 0; a < lengthX; a++) {
			for (int b = 0; b < lenghtY; b++) {
				// Under map faces elevated
				if (baseFacesList.contains(idCurrentFace)) {
					wbPolygonList.add(new WB_Polygon(
									mapMesh.getBaseRaisedPoint(a, b), 
									mapMesh.getBaseRaisedPoint(a+1, b),
									mapMesh.getBaseRaisedPoint(a+1, b+1), 
									mapMesh.getBaseRaisedPoint(a, b+1)));
				} else {
					// Under map faces non elevated
					wbPolygonList.add(new WB_Polygon(
							mapMesh.getBasePoint(a, b), 
							mapMesh.getBasePoint(a+1, b),
							mapMesh.getBasePoint(a+1, b+1), 
							mapMesh.getBasePoint(a, b+1)));

					// Inner right side
					if (baseFacesList.contains(idCurrentFace + 1) && ((idCurrentFace + 1) % lenghtY) != 0) {
						
						wbPolygonList.add(new WB_Polygon(
								mapMesh.getBasePoint(a, b+1),
								mapMesh.getBasePoint(a+1, b+1),
								mapMesh.getBaseRaisedPoint(a+1, b+1),
								mapMesh.getBaseRaisedPoint(a, b+1)));
					}

					// Inner left side
					if (baseFacesList.contains(idCurrentFace - 1) && (idCurrentFace % lenghtY) != 0) { 
						
						wbPolygonList.add(new WB_Polygon(
										mapMesh.getBaseRaisedPoint(a+1, b), 
										mapMesh.getBasePoint(a+1, b),
										mapMesh.getBasePoint(a, b), 
										mapMesh.getBaseRaisedPoint(a, b)));
					}

					// Inner bottom side
					if (baseFacesList.contains(idCurrentFace + lengthX)) { 
						
						wbPolygonList.add(new WB_Polygon(
										mapMesh.getBasePoint(a+1, b), 
										mapMesh.getBaseRaisedPoint(a+1, b),
										mapMesh.getBaseRaisedPoint(a+1, b+1), 
										mapMesh.getBasePoint(a+1, b+1)));
					}

					// Inner top side
					if (baseFacesList.contains(idCurrentFace - lengthX)) { 
						
						wbPolygonList.add(new WB_Polygon(
										mapMesh.getBaseRaisedPoint(a, b+1), 
										mapMesh.getBaseRaisedPoint(a, b),
										mapMesh.getBasePoint(a, b), 
										mapMesh.getBasePoint(a, b+1)));
					}
				}
				idCurrentFace++;
			}
		}
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
		basePointTable[2] = (Config.TOTAL_CLIP_HEIGHT / 2) ;
		basePointTable[3] = ((printSize - Config.MIDDLE_SQUARE_MAP_SIZE) / 2);
		basePointTable[4] = ((printSize - Config.OUTSIDE_WIDTH_CLIP) / 2);
		basePointTable[5] = ((printSize - Config.INSIDE_WIDTH_CLIP) / 2);
		basePointTable[6] = ((printSize + Config.INSIDE_WIDTH_CLIP) / 2);
		basePointTable[7] = ((printSize + Config.OUTSIDE_WIDTH_CLIP) / 2);
		basePointTable[8] = ((printSize + Config.MIDDLE_SQUARE_MAP_SIZE) / 2);
		basePointTable[9] = (printSize - (Config.TOTAL_CLIP_HEIGHT / 2));
		basePointTable[10] = (printSize - (Config.INSIDE_HEIGHT_CLIP / 2));
		basePointTable[11] = printSize;

		return basePointTable;
	}
}