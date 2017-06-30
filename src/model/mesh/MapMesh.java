package model.mesh;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.TreeMap;

import config.Config;
import gnu.trove.map.TLongIntMap;
import gnu.trove.map.hash.TLongIntHashMap;
import wblut.hemesh.HE_Face;
import wblut.hemesh.HE_Halfedge;
import wblut.hemesh.HE_Mesh;
import wblut.hemesh.HE_Vertex;

/**
 * Class Mesh A meshing is a set of geometric forms arranged so as to model
 * objects.It is constituted by summits, connected some to the others by faces.
 * When all Faces are triangles, we speak of triangular meshing Trimesh, or of
 * triangulation according to them Domains.
 * 
 * @author picharles
 */
public class MapMesh extends ObjectMesh {

	private String mapMeshName;
	private static int Map_Mesh_Counter = 1;
	private int mapMeshID = Map_Mesh_Counter++;
	private static final int DEFAULT_MAP_MESH_COUNTER = 1;
	private TreeMap<Double, TreeMap<Double, Point3D>> setOfSurfacePoints = new TreeMap<Double, TreeMap<Double, Point3D>>();
	private TreeMap<Double, TreeMap<Double, Point3D>> setOfBasePoints = new TreeMap<Double, TreeMap<Double, Point3D>>();
	private TreeMap<Double, TreeMap<Double, Point3D>> setOfBaseRaisedPoints = new TreeMap<Double, TreeMap<Double, Point3D>>();
	private TreeMap<Double, TreeMap<Double, Point3D>> setOfBaseRaisedSidePoints = new TreeMap<Double, TreeMap<Double, Point3D>>();

	/**
	 * Constructor af a Mesh
	 */
	public MapMesh(double mapHeight, double mapWidth) {
		this.mapMeshName = "Map"+Config.EXPORT_PREFIX_FILE_NAME + this.mapMeshID;
		Config.Debug("Création : " + mapMeshName + " -> H: " + mapHeight + " W: " + mapWidth);
	}

	/**
	 * Getter of the parcel name
	 * 
	 * @return the parcel name : String
	 */
	public String getMapMeshName() {
		return mapMeshName;
	}

	/**
	 * To string override method
	 * 
	 * @return string
	 */
	@Override
	public String toString() {
		return Config.EXPORT_PREFIX_FILE_NAME + mapMeshID;
	}

	/**
	 * Static method to reset the mesh counter
	 */
	public static void resetMapMeshCounter() {
		Map_Mesh_Counter = DEFAULT_MAP_MESH_COUNTER;
	}

	/**
	 * Methode to add a point into the tree map of set of vertices
	 * 
	 * @param line
	 * @param column
	 * @param Point 3D
	 */
	public void addSurfacePoint(double line, double column, Point3D wb_coords) {
		addPoint(setOfSurfacePoints, line, column, wb_coords);
	}

	/**
	 * Methode to add a point base into the tree map of set of vertices base
	 * 
	 * @param line
	 * @param column
	 * @param Point 3D
	 */
	public void addBasePoint(double line, double column, Point3D wb_coords) {
		addPoint(setOfBasePoints, line, column, wb_coords);
	}
	
	/**
	 * Methode to add a point base raised into the tree map of set of vertices base
	 * 
	 * @param line
	 * @param column
	 * @param Point 3D
	 */
	public void addBaseRaisedPoint(double line, double column, Point3D wb_coords) {	
		addPoint(setOfBaseRaisedPoints, line, column, wb_coords);
	}
	
	
	/**
	 * Methode to add a point base side raised into the tree map of set of vertices base
	 * 
	 * @param line
	 * @param column
	 * @param Point 3D
	 */
	public void addBaseRaisedSidePoint(double line, double column, Point3D wb_coords) {	
		addPoint(setOfBaseRaisedSidePoints, line, column, wb_coords);
	}
	
	
	/**
	 * Methode to get a surface point
	 * 
	 * @param line
	 * @param column
	 * @return the 3D Point (or null)
	 */
	public Point3D getSurfacePoint(double line, double column) {
		return getPoint(setOfSurfacePoints, line, column);
	}

	/**
	 * Methode to get a base point
	 * 
	 * @param line
	 * @param column
	 * @return a 3D Point (or null)
	 */
	public Point3D getBasePoint(double line, double column) {
		return getPoint(setOfBasePoints, line, column);
	}
	
	
	/**
	 * Methode to get a base raised point
	 * 
	 * @param line
	 * @param column
	 * @return a 3D Point (or null)
	 */
	public Point3D getBaseRaisedPoint(double line, double column) {
		return getPoint(setOfBaseRaisedPoints, line, column);
	}

	
	/**
	 * Methode to get a base raised side point
	 * 
	 * @param line
	 * @param column
	 * @return a 3D Point (or null)
	 */
	public Point3D getBaseRaisedSidePoint(double line, double column) {
		return getPoint(setOfBaseRaisedSidePoints, line, column);
	}
	
	
	/**
	 * Generic Methode to add a point base raised into the tree map of set of vertices base
	 * 
	 * @param line
	 * @param column
	 * @param Point 3D
	 */
	private void addPoint(TreeMap<Double, TreeMap<Double, Point3D>> treeMap, double line, double column, Point3D wb_coords) {
		if (!treeMap.containsKey(line)) {
			treeMap.put(line, new TreeMap<Double, Point3D>());
		}
		treeMap.get(line).put(column, wb_coords);
	}
	
	/**
	 * Generic Methode to get a point
	 * 
	 * @param line
	 * @param column
	 * @return Point 3D (or null)
	 */
	public Point3D getPoint(TreeMap<Double, TreeMap<Double, Point3D>> treeMap, double line, double column) {
		if (treeMap.containsKey(line)) {
			TreeMap<Double, Point3D> treeMapPoint = (TreeMap<Double, Point3D>) treeMap.get(line);
			if (treeMapPoint.containsKey(column)) {
				return (Point3D) treeMapPoint.get(column);
			}
		}
		return null;
	}
}
