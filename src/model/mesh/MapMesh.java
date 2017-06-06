package model.mesh;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javafx.scene.shape.TriangleMesh;
import javafx.scene.shape.VertexFormat;

/**
 * Class Mesh A meshing is a set of geometrical forms arranged so as to model
 * objects.It is constituted by summits, connected some to the others by faces.
 * When all Faces are triangles, we speak of triangular meshing trimesh, or of
 * triangulation according to them Domains.
 * 
 * @author picharles
 */
public class MapMesh {

	private TreeMap<Double, TreeMap<Double, Vertices>> setOfMapVertices, setOfMapVerticesBase;
	private LinkedList<Face> setOfMapFaces;
	private TriangleMesh triangleMapMesh;
	private double mapHeight, mapWidth;

	/**
	 * Constructor af a Mesh
	 */
	public MapMesh(double mapHeight, double mapWidth) {		
		this.mapHeight = mapHeight;
		this.mapWidth = mapWidth;
		triangleMapMesh = new TriangleMesh(VertexFormat.POINT_TEXCOORD);
		triangleMapMesh.getTexCoords().addAll(0,0);	
		setOfMapFaces = new LinkedList<>();
		setOfMapVertices = new TreeMap<Double, TreeMap<Double, Vertices>>();
		setOfMapVerticesBase = new TreeMap<Double, TreeMap<Double, Vertices>>();
		Vertices.resetCounter();
	}
	
	
	/**
	 * Method allow to generate a 3D object : create a custom TriangleMesh and add all points and faces
	 */
	public void generate3DObject(){
	
		// Ecriture de l'ensemble des points de la surface
		Set<Map.Entry<Double, TreeMap>> setLine = getSetOfVertices().entrySet();

		Iterator<Map.Entry<Double, TreeMap>> iterator = setLine.iterator();
		
		triangleMapMesh.getPoints().addAll(0,0,0);

		while (iterator.hasNext()) {
			Map.Entry<Double, TreeMap> entry = iterator.next();
			TreeMap verticesTreeMap = entry.getValue();

			Set<Map.Entry<Double, Vertices>> setColumn = verticesTreeMap.entrySet();
			Iterator<Map.Entry<Double, Vertices>> iterator2 = setColumn.iterator();

			while (iterator2.hasNext()) {
				Map.Entry<Double, Vertices> verticesEntry = iterator2.next();
				triangleMapMesh.getPoints().addAll((float) verticesEntry.getValue().getX(), (float) verticesEntry.getValue().getY(), (float) verticesEntry.getValue().getZ());
			}
		}
	
		// Ecriture de l'ensemble des points du socle
		Set<Map.Entry<Double, TreeMap>> setLineBase = getSetOfVerticesBase().entrySet();
		Iterator<Map.Entry<Double, TreeMap>> iterator3 = setLineBase.iterator();
		while (iterator3.hasNext()) {
			Map.Entry<Double, TreeMap> entry2 = iterator3.next();
			TreeMap verticesTreeMapBase = entry2.getValue();

			Set<Map.Entry<Double, Vertices>> setColumnBase = verticesTreeMapBase.entrySet();
			Iterator<Map.Entry<Double, Vertices>> iterator4 = setColumnBase.iterator();

			while (iterator4.hasNext()) {
				Map.Entry<Double, Vertices> verticesEntrySocle = iterator4.next();
				triangleMapMesh.getPoints().addAll((float) verticesEntrySocle.getValue().getX(), (float) verticesEntrySocle.getValue().getY(), (float) verticesEntrySocle.getValue().getZ());			}
		}
		
		for (Face face : getSetOfFaces()) {
			triangleMapMesh.getFaces().addAll(face.getIdVertice1(), 0,face.getIdVertice2(), 0,face.getIdVertice3(),0);
		}
	}

	/**
	 * Getter of the map height
	 * 
	 * @return the map height
	 */
	public double getMapHeight() {
		return mapHeight;
	}

	/**
	 * Getter of the map width
	 * 
	 * @return the map width
	 */
	public double getMapWidth() {
		return mapWidth;
	}

	/**
	 * Getter of the triangle map mesh
	 * 
	 * @return the triangle map mesh
	 */
	public TriangleMesh getTriangleMapMesh(){
		return triangleMapMesh;
	}
	
	/**
	 * Method to set the height of the map
	 * 
	 * @param mapHeight
	 */
	public void setMapHeight(double mapHeight) {
		this.mapHeight = mapHeight;
	}

	/**
	 * Method to set the width of the map
	 * 
	 * @param mapWidth
	 */
	public void setMapWidth(double mapWidth) {
		this.mapWidth = mapWidth;
	}
		

	/**
	 * Getter of a set of vertices
	 * 
	 * @return a tree map of set of vertices
	 */
	public TreeMap getSetOfVertices() {
		return setOfMapVertices;
	}

	/**
	 * Getter of set of faces
	 * 
	 * @return a linked list of set of faces
	 */
	public LinkedList<Face> getSetOfFaces() {
		return setOfMapFaces;
	}

	/**
	 * Getter of set of vertices base
	 * 
	 * @return a tree map of set of vertices base
	 */
	public TreeMap getSetOfVerticesBase() {
		return setOfMapVerticesBase;
	}

	/**
	 * Method to add a face into the set of face map
	 * 
	 * @param face : the face
	 */
	public void addFace(Face face) {
		setOfMapFaces.add(face);
	}

	/**
	 * Method to add a vertices into the tree map of set of vertices
	 * 
	 * @param line
	 * @param column
	 * @param vertices
	 */
	public void addVertices(double line, double column, Vertices vertices) {
		if (!this.setOfMapVertices.containsKey(line)) {
			this.setOfMapVertices.put(line, new TreeMap());
			this.setOfMapVertices.get(line).put(column, vertices);
			
		} else {
			this.setOfMapVertices.get(line).put(column, vertices);
		}
	}
		
	/**
	 * Method to add a vertices base into the tree map of set of vertices base
	 * 
	 * @param line
	 * @param column
	 * @param vertices
	 */
	public void addVerticesBase(double line, double column, Vertices vertices) {
		if (!this.setOfMapVerticesBase.containsKey(line)) {
			this.setOfMapVerticesBase.put(line, new TreeMap());
			this.setOfMapVerticesBase.get(line).put(column, vertices);
		} else {
			this.setOfMapVerticesBase.get(line).put(column, vertices);
		}
	}

	/**
	 * Method to get a surface point
	 * 
	 * @param line
	 * @param column
	 * @return the vertices (or null)
	 */
	public Vertices getSurfacePoint(double line, double column) {
		if (getSetOfVertices().containsKey(line)) {
			TreeMap verticesTreeMap = (TreeMap) getSetOfVertices().get(line);
			if (verticesTreeMap.containsKey(column)) {
				return (Vertices) verticesTreeMap.get(column);
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
	public Vertices getBasePoint(double line, double column) {
		if (getSetOfVerticesBase().containsKey(line)) {
			TreeMap sommetTreeMap = (TreeMap) getSetOfVerticesBase().get(line);
			if (sommetTreeMap.containsKey(column)) {
				return (Vertices) sommetTreeMap.get(column);
			}
		}
		return null;
	}
}
