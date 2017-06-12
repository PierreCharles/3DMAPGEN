package model.mesh;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import config.Config;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.shape.VertexFormat;
import wblut.hemesh.HE_Mesh;

/**
 * Class Mesh A meshing is a set of geometrical forms arranged so as to model
 * objects.It is constituted by summits, connected some to the others by faces.
 * When all Faces are triangles, we speak of triangular meshing trimesh, or of
 * triangulation according to them Domains.
 * 
 * @author picharles
 */
public class MapMesh extends HE_Mesh{

	private TreeMap<Double, TreeMap<Double, Vertices>> setOfMapMeshVertices, setOfMapMeshVerticesBase;
	private LinkedList<Face> setOfMapMeshFaces;
	private TriangleMesh triangleMapMesh;
	private double mapMeshHeight, mapMeshWidth;
	private boolean mapMeshViewIsGenerated = false;
	
	private String mapMeshName;
	private int mapMeshID;
	private static int Map_Mesh_Counter = 1;
	private static final int DEFAULT_MAP_MESH_COUNTER = 1;
	

	/**
	 * Constructor af a Mesh
	 */
	public MapMesh(double mapHeight, double mapWidth) {	
		
		this.mapMeshHeight = mapHeight;
		this.mapMeshWidth = mapWidth;
		
		this.mapMeshID = Map_Mesh_Counter;
		Map_Mesh_Counter++;
		
		triangleMapMesh = new TriangleMesh(VertexFormat.POINT_TEXCOORD);
		triangleMapMesh.getTexCoords().addAll(0,0);	
		
		setOfMapMeshFaces = new LinkedList<>();
		setOfMapMeshVertices = new TreeMap<Double, TreeMap<Double, Vertices>>();
		setOfMapMeshVerticesBase = new TreeMap<Double, TreeMap<Double, Vertices>>();
		Vertices.resetCounter();
	}
	
	
	/**
	 * Method allow to generate a 3D object : create a custom TriangleMesh and add all points and faces
	 */
	public void generate3DObject(){
		
		if(!mapMeshViewIsGenerated){
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
					triangleMapMesh.getPoints().addAll((float) verticesEntrySocle.getValue().getX(), (float) verticesEntrySocle.getValue().getY(), (float) verticesEntrySocle.getValue().getZ());
				}
			}
			
			for (Face face : getSetOfFaces()) {
				triangleMapMesh.getFaces().addAll(face.getIdVertice1(), 0,face.getIdVertice2(), 0,face.getIdVertice3(),0);
			}
			mapMeshViewIsGenerated = true;
		}		
	}
	
	
	/**
	 * Static method to reset the mesh counter
	 */
	public static void resetMapMeshCounter(){
		Map_Mesh_Counter = DEFAULT_MAP_MESH_COUNTER;
	}
	
	
	/**
	 * Method to export a MeshMap object file
	 * 
	 * @param mesh
	 * @param destinationFile
	 * @param directoryName
	 * @param numberOfPart
	 * @throws IOException
	 */
	public void exportMapMeshToObj(String destination){
		
		File file = new File(destination + "\\" +this.mapMeshName +".obj");
		
		try (FileWriter fileWriter = new FileWriter(file)) {
			
			fileWriter.write("# 3DGenMap - File generator\r\n");
			
			// Ecriture de l'ensemble des points de la surface
			Set<Map.Entry<Double, TreeMap>> setLine = this.getSetOfVertices().entrySet();

			Iterator<Map.Entry<Double, TreeMap>> iterator = setLine.iterator();

			while (iterator.hasNext()) {
				Map.Entry<Double, TreeMap> entry = iterator.next();
				TreeMap verticesTreeMap = entry.getValue();

				Set<Map.Entry<Double, Vertices>> setColumn = verticesTreeMap.entrySet();
				Iterator<Map.Entry<Double, Vertices>> iterator2 = setColumn.iterator();

				while (iterator2.hasNext()) {
					Map.Entry<Double, Vertices> verticesEntry = iterator2.next();
					fileWriter.write(verticesEntry.getValue().toString());
					
				}
			}
		
			// Ecriture de l'ensemble des points du socle
			Set<Map.Entry<Double, TreeMap>> setLineBase = this.getSetOfVerticesBase().entrySet();
			Iterator<Map.Entry<Double, TreeMap>> iterator3 = setLineBase.iterator();
			while (iterator3.hasNext()) {
				Map.Entry<Double, TreeMap> entry2 = iterator3.next();
				TreeMap verticesTreeMapBase = entry2.getValue();

				Set<Map.Entry<Double, Vertices>> setColumnBase = verticesTreeMapBase.entrySet();
				Iterator<Map.Entry<Double, Vertices>> iterator4 = setColumnBase.iterator();

				while (iterator4.hasNext()) {
					Map.Entry<Double, Vertices> verticesEntrySocle = iterator4.next();
					fileWriter.write(verticesEntrySocle.getValue().toString());
				}
			}
			
			for (Face face : this.getSetOfFaces()) {
				fileWriter.write(face.toString());
			}
			
			fileWriter.close();
			
			Config.Debug(this.mapMeshName +" exporté dans "+file.getAbsolutePath());
			
		} catch (IOException e) {
			Config.Debug(e.getMessage());
			e.printStackTrace();
		}
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
	 * Getter of the partel ID
	 * 
	 * @return the partel id : int
	 */
	public int getMapMeshID(){
		return mapMeshID;
	}

	/**
	 * Setter of the parcel name
	 * 
	 * @param parcelName : string
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
	 * @return the triangle map mesh :  TriangleMesh
	 */
	public TriangleMesh getTriangleMapMesh(){
		return triangleMapMesh;
	}
	
	/**
	 * Method to set the height of the map
	 * 
	 * @param mapMeshHeight : double
	 */
	public void setMapMeshHeight(double mapMeshHeight) {
		this.mapMeshHeight = mapMeshHeight;
	}

	/**
	 * Method to set the width of the map
	 * 
	 * @param mapMeshWidth : double
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
	 * Getter of set of faces
	 * 
	 * @return a linked list of set of faces
	 */
	public LinkedList<Face> getSetOfFaces() {
		return setOfMapMeshFaces;
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
	 * Method to add a face into the set of face map
	 * 
	 * @param face : the face
	 */
	public void addFace(Face face) {
		setOfMapMeshFaces.add(face);
	}
	

	/**
	 * To string override method
	 * 
	 * @return string 
	 */
	@Override
	public String toString() {
		return Config.EXPORT_PREFIX_FILE_NAME+mapMeshID;
	}	

	/**
	 * Method to add a vertices into the tree map of set of vertices
	 * 
	 * @param line
	 * @param column
	 * @param vertices
	 */
	public void addVertices(double line, double column, Vertices vertices) {
		if (!this.setOfMapMeshVertices.containsKey(line)) {
			this.setOfMapMeshVertices.put(line, new TreeMap());
			this.setOfMapMeshVertices.get(line).put(column, vertices);
			
		} else {
			this.setOfMapMeshVertices.get(line).put(column, vertices);
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
		if (!this.setOfMapMeshVerticesBase.containsKey(line)) {
			this.setOfMapMeshVerticesBase.put(line, new TreeMap());
			this.setOfMapMeshVerticesBase.get(line).put(column, vertices);
		} else {
			this.setOfMapMeshVerticesBase.get(line).put(column, vertices);
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
