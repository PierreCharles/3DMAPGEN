package model.mesh;

import config.Config;
import wblut.geom.WB_Polygon;
import wblut.hemesh.HEC_FromPolygons;
import wblut.hemesh.HE_Mesh;

/**
 * Class representing a mesh object of type clip
 * This class extends of ObjectMesh
 * 
 * @author picharles
 *
 */
public class ClipMesh extends ObjectMesh{
	

	private static int Clip_Mesh_Counter = 1;
	
	/**
	 * Constructor of a Clip Mesh
	 * Create points and then create faces
	 */
	public ClipMesh(){
		super("ClipMesh", Clip_Mesh_Counter++);
		Point3D[] clipPoints = clipPointsCreator();
		WB_Polygon[] wb_polygons = generatePolygonFaces(clipPoints);
		HE_Mesh he_mesh = new HE_Mesh(new HEC_FromPolygons(wb_polygons));
		this.setHe_mesh(he_mesh);	
	}
	
	
	/**
	 * Method used for create clip points
	 * 
	 * Class to generate a clip
	 * 
	 * @return a table of Point3D
	 *  ___              ___
	 * |   | __________ |   |
	 * |                    |
	 * |     __________     |
	 * |___|            |___|
	 */
	private Point3D[] clipPointsCreator(){
		
		Point3D[] clipPoints = new Point3D[24];
		
		clipPoints[0] = new Point3D(0,0,0);
		clipPoints[1] = new Point3D(0,Config.OUTSIDE_WIDTH_CLIP,0);
		clipPoints[2] = new Point3D((Config.TOTAL_CLIP_HEIGHT-Config.INSIDE_HEIGHT_CLIP)/2,Config.OUTSIDE_WIDTH_CLIP,0);
		clipPoints[3] = new Point3D((Config.TOTAL_CLIP_HEIGHT-Config.INSIDE_HEIGHT_CLIP)/2,0,0);
		
		clipPoints[4] = new Point3D((Config.TOTAL_CLIP_HEIGHT-Config.INSIDE_HEIGHT_CLIP)/2, (Config.OUTSIDE_WIDTH_CLIP-Config.INSIDE_WIDTH_CLIP)/2,0);
		clipPoints[5] = new Point3D((Config.TOTAL_CLIP_HEIGHT-Config.INSIDE_HEIGHT_CLIP)/2,(Config.OUTSIDE_WIDTH_CLIP+Config.INSIDE_WIDTH_CLIP)/2,0);
		clipPoints[6] = new Point3D((Config.TOTAL_CLIP_HEIGHT+Config.INSIDE_HEIGHT_CLIP)/2, (Config.OUTSIDE_WIDTH_CLIP+Config.INSIDE_WIDTH_CLIP)/2,0);
		clipPoints[7] = new Point3D((Config.TOTAL_CLIP_HEIGHT+Config.INSIDE_HEIGHT_CLIP)/2,(Config.OUTSIDE_WIDTH_CLIP-Config.INSIDE_WIDTH_CLIP)/2,0);
		
		clipPoints[8] = new Point3D((Config.TOTAL_CLIP_HEIGHT+Config.INSIDE_HEIGHT_CLIP)/2,0,0);
		clipPoints[9] = new Point3D((Config.TOTAL_CLIP_HEIGHT+Config.INSIDE_HEIGHT_CLIP)/2,Config.OUTSIDE_WIDTH_CLIP,0);
		clipPoints[10] = new Point3D(Config.TOTAL_CLIP_HEIGHT, Config.OUTSIDE_WIDTH_CLIP, 0);
		clipPoints[11] = new Point3D(Config.TOTAL_CLIP_HEIGHT, 0,0);
		
		clipPoints[12] = new Point3D(0,0,Config.BASE_MAP_RAISED_TICKNESS);
		clipPoints[13] = new Point3D(0,Config.OUTSIDE_WIDTH_CLIP,Config.BASE_MAP_RAISED_TICKNESS);
		clipPoints[14] = new Point3D((Config.TOTAL_CLIP_HEIGHT-Config.INSIDE_HEIGHT_CLIP)/2,Config.OUTSIDE_WIDTH_CLIP,Config.BASE_MAP_RAISED_TICKNESS);
		clipPoints[15] = new Point3D((Config.TOTAL_CLIP_HEIGHT-Config.INSIDE_HEIGHT_CLIP)/2,0,Config.BASE_MAP_RAISED_TICKNESS);
		
		clipPoints[16] = new Point3D((Config.TOTAL_CLIP_HEIGHT-Config.INSIDE_HEIGHT_CLIP)/2, (Config.OUTSIDE_WIDTH_CLIP-Config.INSIDE_WIDTH_CLIP)/2,Config.BASE_MAP_RAISED_TICKNESS);
		clipPoints[17] = new Point3D((Config.TOTAL_CLIP_HEIGHT-Config.INSIDE_HEIGHT_CLIP)/2,(Config.OUTSIDE_WIDTH_CLIP+Config.INSIDE_WIDTH_CLIP)/2,Config.BASE_MAP_RAISED_TICKNESS);
		clipPoints[18] = new Point3D((Config.TOTAL_CLIP_HEIGHT+Config.INSIDE_HEIGHT_CLIP)/2, (Config.OUTSIDE_WIDTH_CLIP+Config.INSIDE_WIDTH_CLIP)/2,Config.BASE_MAP_RAISED_TICKNESS);
		clipPoints[19] = new Point3D((Config.TOTAL_CLIP_HEIGHT+Config.INSIDE_HEIGHT_CLIP)/2,(Config.OUTSIDE_WIDTH_CLIP-Config.INSIDE_WIDTH_CLIP)/2,Config.BASE_MAP_RAISED_TICKNESS);
		
		clipPoints[20] = new Point3D((Config.TOTAL_CLIP_HEIGHT+Config.INSIDE_HEIGHT_CLIP)/2,0,Config.BASE_MAP_RAISED_TICKNESS);
		clipPoints[21] = new Point3D((Config.TOTAL_CLIP_HEIGHT+Config.INSIDE_HEIGHT_CLIP)/2,Config.OUTSIDE_WIDTH_CLIP,Config.BASE_MAP_RAISED_TICKNESS);
		clipPoints[22] = new Point3D(Config.TOTAL_CLIP_HEIGHT, Config.OUTSIDE_WIDTH_CLIP, Config.BASE_MAP_RAISED_TICKNESS);
		clipPoints[23] = new Point3D(Config.TOTAL_CLIP_HEIGHT, 0,Config.BASE_MAP_RAISED_TICKNESS);
		
		return clipPoints;
	}
	
	/**
	 * Metode used for create clip faces with an array of 3DPoints
	 * 
	 * @param clipPoints
	 * @return an array of polygons faces
	 */
	private WB_Polygon[] generatePolygonFaces(Point3D[] clipPoints){
		
		WB_Polygon[] wb_polygons = new WB_Polygon[18];

		// Surface
		wb_polygons[0] = new WB_Polygon(clipPoints[0], clipPoints[1], clipPoints[2], clipPoints[5],clipPoints[4],clipPoints[3]);
		wb_polygons[1] = new WB_Polygon(clipPoints[4], clipPoints[5], clipPoints[6], clipPoints[7]);
		wb_polygons[2] = new WB_Polygon(clipPoints[8], clipPoints[7], clipPoints[6], clipPoints[9], clipPoints[10], clipPoints[11]);
		
		// Outside
		wb_polygons[3] = new WB_Polygon(clipPoints[15], clipPoints[16], clipPoints[17], clipPoints[14], clipPoints[13],  clipPoints[12]);
		wb_polygons[4] = new WB_Polygon(clipPoints[19], clipPoints[18], clipPoints[17], clipPoints[16]);
		wb_polygons[5] = new WB_Polygon(clipPoints[23], clipPoints[22], clipPoints[21], clipPoints[18], clipPoints[19], clipPoints[20]);
		
		// Top
		wb_polygons[6] = new WB_Polygon(clipPoints[12], clipPoints[13], clipPoints[1], clipPoints[0]);
		
		// Bottom
		wb_polygons[7] = new WB_Polygon(clipPoints[22], clipPoints[23], clipPoints[11], clipPoints[10]);
		
		// Left sides
		wb_polygons[8] = new WB_Polygon(clipPoints[0], clipPoints[3], clipPoints[15], clipPoints[12]);
		wb_polygons[9] = new WB_Polygon(clipPoints[4], clipPoints[7], clipPoints[19], clipPoints[16]);
		wb_polygons[10] = new WB_Polygon(clipPoints[8], clipPoints[11], clipPoints[23], clipPoints[20]);
		wb_polygons[11] = new WB_Polygon(clipPoints[3], clipPoints[4], clipPoints[16], clipPoints[15]);
		wb_polygons[12] = new WB_Polygon(clipPoints[7], clipPoints[8], clipPoints[20], clipPoints[19]);

		// Right side
		wb_polygons[13] = new WB_Polygon(clipPoints[13], clipPoints[14], clipPoints[2], clipPoints[1]);
		wb_polygons[14] = new WB_Polygon(clipPoints[17], clipPoints[18], clipPoints[6], clipPoints[5]);
		wb_polygons[15] = new WB_Polygon(clipPoints[21], clipPoints[22], clipPoints[10], clipPoints[9]);
		wb_polygons[16] = new WB_Polygon(clipPoints[14], clipPoints[17], clipPoints[5], clipPoints[2]);
		wb_polygons[17] = new WB_Polygon(clipPoints[9], clipPoints[6], clipPoints[18], clipPoints[21]);
		
		return wb_polygons;
	}
	
}
