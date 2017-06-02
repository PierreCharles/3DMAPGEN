package model.mesh;

import static model.mesh.Vertices.resetCounter;


import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.shape.VertexFormat;

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
	
	
	
	public MeshView generate3DObject(){
		TriangleMesh triangleMesh = new TriangleMesh();
		
		int counterPoint = 0;
		
		
		
		triangleMesh.vertexFormatProperty().setValue(VertexFormat.POINT_TEXCOORD);
		
		triangleMesh.getTexCoords().addAll(0,0);
		
		// Ecriture de l'ensemble des points de la surface
		Set<Map.Entry<Double, TreeMap>> setLine = getSetOfVertices().entrySet();
		Iterator<Map.Entry<Double, TreeMap>> iterator = setLine.iterator();
		while (iterator.hasNext()) {
			Map.Entry<Double, TreeMap> entry = iterator.next();
			TreeMap verticesTreeMap = entry.getValue();

			Set<Map.Entry<Double, Vertices>> setColumn = verticesTreeMap.entrySet();
			Iterator<Map.Entry<Double, Vertices>> iterator2 = setColumn.iterator();

			while (iterator2.hasNext()) {
				Map.Entry<Double, Vertices> verticesEntry = iterator2.next();
				if(verticesEntry.getValue().getId()!=counterPoint){
					//System.out.println(verticesEntry.getValue().getId()+ " "+counterPoint);
				}
				triangleMesh.getPoints().addAll((float) verticesEntry.getValue().getX(), (float) verticesEntry.getValue().getY(), (float) verticesEntry.getValue().getZ());
				counterPoint++;
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
				if(verticesEntrySocle.getValue().getId()!=counterPoint){
					//System.out.println(verticesEntrySocle.getValue().getId()+ " "+counterPoint);
				}
				triangleMesh.getPoints().addAll((float) verticesEntrySocle.getValue().getX(), (float) verticesEntrySocle.getValue().getY(), (float) verticesEntrySocle.getValue().getZ());
				counterPoint++;
			}
		}
		
		
		for (Face face : getSetOfFaces()) {
			triangleMesh.getFaces().addAll(face.getIdVertice1(), 0,face.getIdVertice2(), 0,face.getIdVertice3(),0);
		}
		
		
		System.out.println("1 nb element dans liste points. : "+triangleMesh.getPointElementSize());
		System.out.println("2 nb element dans liste de face: "+triangleMesh.getFaceElementSize());
		System.out.println("3 nb element dans liste text coord : "+triangleMesh.getTexCoordElementSize());
		System.out.println("4 taille liste point: "+triangleMesh.getPoints().size());
		System.out.println("5 taille liste face: "+triangleMesh.getFaces().size());
		System.out.println("6 taille liste textcoord: "+triangleMesh.getTexCoords().size());
	
		
		return new MeshView(triangleMesh);
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
