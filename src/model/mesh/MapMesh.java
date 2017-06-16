package model.mesh;

import java.util.TreeMap;
import config.Config;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.shape.VertexFormat;
import wblut.hemesh.HET_Export;
import wblut.hemesh.HE_Mesh;

/**
 * Class Mesh A meshing is a set of geometric forms arranged so as to model
 * objects.It is constituted by summits, connected some to the others by faces.
 * When all Faces are triangles, we speak of triangular meshing Trimesh, or of
 * triangulation according to them Domains.
 * 
 * @author picharles
 */
public class MapMesh {

	private TreeMap<Double, TreeMap<Double, Point3D>> setOfMapMeshVertices, setOfMapMeshVerticesBase;
	private TriangleMesh triangleMapMesh;
	private double mapMeshHeight, mapMeshWidth;
	private String mapMeshName;
	private int mapMeshID;
	private static int Map_Mesh_Counter = 1;
	private static final int DEFAULT_MAP_MESH_COUNTER = 1;
	private HE_Mesh he_mesh;

	/**
	 * Constructor af a Mesh
	 */
	public MapMesh(double mapHeight, double mapWidth) {

		this.mapMeshHeight = mapHeight;
		this.mapMeshWidth = mapWidth;

		this.mapMeshID = Map_Mesh_Counter++;
		this.mapMeshName = Config.EXPORT_PREFIX_FILE_NAME + this.mapMeshID;

		triangleMapMesh = new TriangleMesh(VertexFormat.POINT_TEXCOORD);
		triangleMapMesh.getTexCoords().addAll(0, 0);

		setOfMapMeshVertices = new TreeMap<Double, TreeMap<Double, Point3D>>();
		setOfMapMeshVerticesBase = new TreeMap<Double, TreeMap<Double, Point3D>>();

		Config.Debug("Création d'une MapMesh: " + mapMeshName + " -> H: " + mapMeshHeight + " W: " + mapMeshWidth);
	}

	/**
	 * Method to get the HE_Mesh object
	 * 
	 * @return HE_Mesh object
	 */
	public HE_Mesh getHe_mesh() {
		return he_mesh;
	}

	/**
	 * Setter of the HE_Mesh object of the current partel map
	 * 
	 * @param he_mesh
	 */
	public void setHe_mesh(HE_Mesh he_mesh) {
		this.he_mesh = he_mesh;
	}

	/**
	 * Method to generate a 3D object for the 3D Viewer
	 */
	public void generate3DObject() {
		// TODO NOT IMPLETED YET
	}

	/**
	 * Static method to reset the mesh counter
	 */
	public static void resetMapMeshCounter() {
		Map_Mesh_Counter = DEFAULT_MAP_MESH_COUNTER;
	}

	/**
	 * Method to export a MeshMap object file
	 * 
	 * @param destination
	 */
	public void exportMapMeshToObj(String destination) {
		HET_Export.saveToOBJ(this.he_mesh, destination, this.mapMeshName);
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
	 * Getter of the parcel map mesh ID
	 * 
	 * @return the parcel id : an integer
	 */
	public int getMapMeshID() {
		return mapMeshID;
	}

	/**
	 * Setter of the parcel mesh name
	 * 
	 * @param parcelName
	 *            : string
	 */
	public void setMapMeshName(String meshName) {
		this.mapMeshName = meshName;
	}

	/**
	 * Getter of the map height
	 * 
	 * @return the map height
	 */
	public double getMapMeshHeight() {
		return mapMeshHeight;
	}

	/**
	 * Getter of the map width
	 * 
	 * @return the map width
	 */
	public double getMapMeshWidth() {
		return mapMeshWidth;
	}

	/**
	 * Getter of the triangle map mesh
	 * 
	 * @return the triangle map mesh : TriangleMesh
	 */
	public TriangleMesh getTriangleMapMesh() {
		return triangleMapMesh;
	}

	/**
	 * Method to set the height of the map
	 * 
	 * @param mapMeshHeight
	 *            : double
	 */
	public void setMapMeshHeight(double mapMeshHeight) {
		this.mapMeshHeight = mapMeshHeight;
	}

	/**
	 * Method to set the width of the map
	 * 
	 * @param mapMeshWidth
	 *            : double
	 */
	public void setMapMeshWidth(double mapMeshWidth) {
		this.mapMeshWidth = mapMeshWidth;
	}

	/**
	 * Getter of a set of vertices
	 * 
	 * @return a tree map of set of vertices
	 */
	public TreeMap getSetOfVertices() {
		return setOfMapMeshVertices;
	}

	/**
	 * Getter of set of vertices base
	 * 
	 * @return a tree map of set of vertices base
	 */
	public TreeMap getSetOfVerticesBase() {
		return setOfMapMeshVerticesBase;
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
	 * Method to add a vertices into the tree map of set of vertices
	 * 
	 * @param line
	 * @param column
	 * @param wb_coords
	 */
	public void addVertices(double line, double column, Point3D wb_coords) {
		if (!this.setOfMapMeshVertices.containsKey(line)) {
			this.setOfMapMeshVertices.put(line, new TreeMap());
			this.setOfMapMeshVertices.get(line).put(column, wb_coords);

		} else {
			this.setOfMapMeshVertices.get(line).put(column, wb_coords);
		}
	}

	/**
	 * Method to add a vertices base into the tree map of set of vertices base
	 * 
	 * @param line
	 * @param column
	 * @param vertices
	 */
	public void addVerticesBase(double line, double column, Point3D wb_coords) {
		if (!this.setOfMapMeshVerticesBase.containsKey(line)) {
			this.setOfMapMeshVerticesBase.put(line, new TreeMap());
			this.setOfMapMeshVerticesBase.get(line).put(column, wb_coords);
		} else {
			this.setOfMapMeshVerticesBase.get(line).put(column, wb_coords);
		}
	}

	/**
	 * Method to get a surface point
	 * 
	 * @param line
	 * @param column
	 * @return the vertices (or null)
	 */
	public Point3D getSurfacePoint(double line, double column) {
		if (getSetOfVertices().containsKey(line)) {
			TreeMap verticesTreeMap = (TreeMap) getSetOfVertices().get(line);
			if (verticesTreeMap.containsKey(column)) {
				return (Point3D) verticesTreeMap.get(column);
			}
		}
		return null;
	}

	/**
	 * Method to get a base point
	 * 
	 * @param line
	 * @param column
	 * @return a vertices (or null)
	 */
	public Point3D getBasePoint(double line, double column) {
		if (getSetOfVerticesBase().containsKey(line)) {
			TreeMap sommetTreeMap = (TreeMap) getSetOfVerticesBase().get(line);
			if (sommetTreeMap.containsKey(column)) {
				return (Point3D) sommetTreeMap.get(column);
			}
		}
		return null;
	}
}
