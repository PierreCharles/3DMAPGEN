package model.mesh;

import static model.mesh.Vertices.resetCounter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.TreeMap;

/**
 * Class Mesh A meshing is a set of geometrical forms arranged so as to model
 * objects.It is constituted by summits, connected some to the others by faces.
 * When all Faces are triangles, we speak of triangular meshing trimesh, or of
 * triangulation according to them Domains.
 * 
 * @author
 */
public class Mesh {

	private TreeMap<Double, TreeMap<Double, Vertices>> setOfVertices, setOfVerticesBase;
	private LinkedList<Face> setOfFaces;

	/**
	 * Constructor af a Mesh
	 */
	public Mesh() {
		setOfFaces = new LinkedList();
		setOfVertices = new TreeMap<Double, TreeMap<Double, Vertices>>();
		setOfVerticesBase = new TreeMap<Double, TreeMap<Double, Vertices>>();
		resetCounter();
	}

	/**
	 * Getter of a set of vertices
	 * 
	 * @return a tree map of set of vertices
	 */
	public TreeMap getSetOfVertices() {
		return setOfVertices;
	}

	/**
	 * Getter of set of faces
	 * 
	 * @return a linked list of set of faces
	 */
	public LinkedList<Face> getSetOfFaces() {
		return setOfFaces;
	}

	/**
	 * Getter of set of vertices base
	 * 
	 * @return a tree map of set of vertices base
	 */
	public TreeMap getSetOfVerticesBase() {
		return setOfVerticesBase;
	}

	/**
	 * Method for add a face into the linked list
	 * 
	 * @param face : the face
	 */
	public void addFace(Face face) {
		setOfFaces.add(face);
	}

	/**
	 * Method for count the size of faces linked list
	 * 
	 * @return
	 */
	public int countFace() {
		return setOfFaces.size();
	}

	/**
	 * Method for add a vertices into the tree map of set of vertices
	 * 
	 * @param line
	 * @param column
	 * @param vertices
	 */
	public void addVertices(double line, double column, Vertices vertices) {
		if (!this.setOfVertices.containsKey(line)) {
			this.setOfVertices.put(line, new TreeMap());
			this.setOfVertices.get(line).put(column, vertices);
		} else {
			this.setOfVertices.get(line).put(column, vertices);
		}
	}

	/**
	 * Method for add a vertices base into the tree map of set of vertices base
	 * 
	 * @param line
	 * @param column
	 * @param vertices
	 */
	public void addVerticesBase(double line, double column, Vertices vertices) {
		if (!this.setOfVerticesBase.containsKey(line)) {
			this.setOfVerticesBase.put(line, new TreeMap());
			this.setOfVerticesBase.get(line).put(column, vertices);
		} else {
			this.setOfVerticesBase.get(line).put(column, vertices);
		}
	}

	/**
	 * Method for get a surface point
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
	 * Method for get a base point
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
