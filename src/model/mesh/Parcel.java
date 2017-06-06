package model.mesh;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import config.Config;

/**
 * Class Parcel
 * 
 * @author picharles
 */
public class Parcel {

	private MapMesh mapMesh;
	private String parcelName;
	private int partelID;
	private static int Partel_Counter = 1;
	private static final int DEFAULT_PARTEL_COUNTER = 1;

	/**
	 * Constructor of a Parcel
	 * 
	 * @param mapMesh the map mesh
	 */
	public Parcel(MapMesh mapMesh){
		this.mapMesh = mapMesh;
		this.partelID = Partel_Counter;
		this.parcelName = new String(Config.EXPORT_FILE_NAME+partelID);
		Partel_Counter++;
	}
	
	/**
	 * Static method to reset the Parcel counter
	 */
	public static void resetCounter(){
		Partel_Counter = DEFAULT_PARTEL_COUNTER;
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
	public void exportPartelMapMeshToObj(String destination){
		
		File file = new File(destination + "\\" +this.parcelName +".obj");
		
		try (FileWriter fileWriter = new FileWriter(file)) {
			
			fileWriter.write("# 3DGenMap - File generator\r\n");
			
			// Ecriture de l'ensemble des points de la surface
			Set<Map.Entry<Double, TreeMap>> setLine = mapMesh.getSetOfVertices().entrySet();

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
			Set<Map.Entry<Double, TreeMap>> setLineBase = mapMesh.getSetOfVerticesBase().entrySet();
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
			
			for (Face face : mapMesh.getSetOfFaces()) {
				fileWriter.write(face.toString());
			}
			
			fileWriter.close();
			
			Config.Debug(this.parcelName +" exporté dans "+file.getAbsolutePath());
			
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
	public String getParcelName() {
		return parcelName;
	}
	
	/**
	 * Getter of the partel ID
	 * 
	 * @return the partel id : int
	 */
	public int getPartelID(){
		return partelID;
	}

	/**
	 * Setter of the parcel name
	 * 
	 * @param parcelName : string
	 */
	public void setParcelName(String parcelName) {
		this.parcelName = parcelName;
	}

	/**
	 * Getter of the MapMesh
	 * 
	 * @return the map mesh : MapMesh
	 */
	public MapMesh getMapMesh() {
		return mapMesh;
	}

	/**
	 * To string override method
	 * 
	 * @return parcel name : String
	 */
	@Override
	public String toString() {
		return parcelName;
	}	

}
