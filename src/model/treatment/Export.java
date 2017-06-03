package model.treatment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import config.Config;
import model.mesh.Face;
import model.mesh.MapMesh;
import model.mesh.Vertices;

/**
 * Class Export Export a mesh into a folder
 * 
 * @author
 *
 */
public class Export {

	/**
	 * Methof for export a Mesh object file(s)
	 * 
	 * @param mesh
	 * @param destinationFile
	 * @param directoryName
	 * @param numberOfPart
	 * @throws IOException
	 */
	static public void exportToObj(MapMesh mesh, String destinationFile, String directoryName, int numberOfPart)
			throws IOException {
		File file = new File(
				destinationFile + "\\" + directoryName + "\\" + directoryName + "Part" + numberOfPart + ".obj");
		try (FileWriter fileWriter = new FileWriter(file)) {
			
			fileWriter.write("# 3DGenMap - File generator\r\n");

			// Ecriture de l'ensemble des points de la surface
			Set<Map.Entry<Double, TreeMap>> setLine = mesh.getSetOfVertices().entrySet();
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
			Set<Map.Entry<Double, TreeMap>> setLineBase = mesh.getSetOfVerticesBase().entrySet();
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

			for (Face face : mesh.getSetOfFaces()) {
				fileWriter.write(face.toString());
			}

			fileWriter.close();
			if(Config.DEBUG){
				System.out.println("Fichier exportÃ© dans");
				System.out.println(file.getAbsolutePath());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method use for create a directory
	 * 
	 * @param destFile
	 * @param dirName
	 */
	static public void createDirectory(String destFile, String dirName) {
		String path = destFile + "\\" + dirName;
		File file = new File(path);
		file.mkdir();
	}

	/**
	 * Method for export an attache mesh to object
	 * 
	 * @param destinationFile
	 * @param directoryName
	 * @param attacheMesh
	 */
	// TO DO -> A verifier - ne fonctionne pas correctement
	static public void exportAttacheMeshToObject(String destinationFile, String directoryName, MapMesh attacheMesh, MapGenerator treatment) {
		File file = new File(destinationFile + "\\" + directoryName + "\\" + "Attache.obj");
		try (FileWriter fileWriter = new FileWriter(file)) {
			fileWriter.write("# 3DGenMap - File generator\r\n");
			fileWriter.write("# Pièce à imprimer "
					+ treatment.calculateNumberOfClip(treatment.getWidthCutNumber(), treatment.getHeightCutNumber()).toString() + " fois\n");
			fileWriter.write("o attache\n\n");
			Set set = attacheMesh.getSetOfVertices().entrySet();
			Iterator iterator = set.iterator();
			while (iterator.hasNext()) {
				Map.Entry entry = (Map.Entry) iterator.next();
				fileWriter.write(entry.getValue().toString());
			}
			for (Face face : attacheMesh.getSetOfFaces()) {
				fileWriter.write(face.toString());
			}
			if(Config.DEBUG){
				System.out.println("attache exportée dans : "+file.getAbsolutePath());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
