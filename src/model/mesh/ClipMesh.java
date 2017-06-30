package model.mesh;

import config.Config;
import wblut.geom.WB_Quad;
import wblut.hemesh.HEC_FromQuads;
import wblut.hemesh.HE_Mesh;

/**
 * Class to generate a clip
 * 
 * @return Clip structure 
 * 3___4           11___12
 * |   |5__________7|   |
 * |                    |
 * |    6__________8    |
 * |___|            |___|
 * 1   2           9    10
 */
public class ClipMesh extends ObjectMesh{
	

	private static int Clip_Mesh_Counter = 1;
	
	public ClipMesh(){
		super("ClipMesh", Clip_Mesh_Counter++);

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
		

		WB_Quad wb_quads[] = new WB_Quad[18];

		// Surface
		wb_quads[0] = new WB_Quad(clipPoints[0], clipPoints[1], clipPoints[2], clipPoints[3]);
		wb_quads[1] = new WB_Quad(clipPoints[4], clipPoints[5], clipPoints[6], clipPoints[7]);
		wb_quads[2] = new WB_Quad(clipPoints[8], clipPoints[9], clipPoints[10], clipPoints[11]);
		
		// Outside
		wb_quads[3] = new WB_Quad(clipPoints[15], clipPoints[14], clipPoints[13], clipPoints[12]);
		wb_quads[4] = new WB_Quad(clipPoints[19], clipPoints[18], clipPoints[17], clipPoints[16]);
		wb_quads[5] = new WB_Quad(clipPoints[23], clipPoints[22], clipPoints[21], clipPoints[20]);
		
		// Top
		wb_quads[6] = new WB_Quad(clipPoints[12], clipPoints[13], clipPoints[1], clipPoints[0]);
		
		// Bottom
		wb_quads[7] = new WB_Quad(clipPoints[22], clipPoints[23], clipPoints[11], clipPoints[10]);
		
		// Left sides
		wb_quads[8] = new WB_Quad(clipPoints[0], clipPoints[3], clipPoints[15], clipPoints[12]);
		wb_quads[9] = new WB_Quad(clipPoints[4], clipPoints[7], clipPoints[19], clipPoints[16]);
		wb_quads[10] = new WB_Quad(clipPoints[8], clipPoints[11], clipPoints[23], clipPoints[20]);
		wb_quads[11] = new WB_Quad(clipPoints[3], clipPoints[4], clipPoints[16], clipPoints[15]);
		wb_quads[12] = new WB_Quad(clipPoints[7], clipPoints[8], clipPoints[20], clipPoints[19]);

		// Right side
		wb_quads[13] = new WB_Quad(clipPoints[13], clipPoints[14], clipPoints[2], clipPoints[1]);
		wb_quads[14] = new WB_Quad(clipPoints[17], clipPoints[18], clipPoints[6], clipPoints[5]);
		wb_quads[15] = new WB_Quad(clipPoints[21], clipPoints[22], clipPoints[10], clipPoints[9]);
		wb_quads[16] = new WB_Quad(clipPoints[14], clipPoints[17], clipPoints[5], clipPoints[2]);
		wb_quads[17] = new WB_Quad(clipPoints[9], clipPoints[6], clipPoints[18], clipPoints[21]);
	
		this.setHe_mesh(new HE_Mesh(new HEC_FromQuads(wb_quads)));	
	}

}
