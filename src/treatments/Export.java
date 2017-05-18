package treatments;

import static treatments.Treatment.getNumberOfClip;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import mesh.Face;
import mesh.Mesh;
import mesh.Vertices;

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
	static public void exportToObj(Mesh mesh, String destinationFile, String directoryName, int numberOfPart)
			throws IOException {
		File file = new File(
				destinationFile + "\\" + directoryName + "\\" + directoryName + "Partie" + numberOfPart + ".obj");
		try (FileWriter fileWriter = new FileWriter(file)) {
			fileWriter.write("# Fichier réalisé par\r\n");
			fileWriter.write("# Alexis Dardinier\r\n");
			fileWriter.write("# Thomas Klein\r\n");
			fileWriter.write("# Pierre Petit\r\n");
			fileWriter.write("# Timothé Rouzé\r\n");
			fileWriter.write("# Mathieu Vincent\r\n");
			fileWriter.write("o maillage\r\n");

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
			System.out.println("Fichier exporté dans");
			System.out.println(file.getAbsolutePath());
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
		File dir = new File(path);
		dir.mkdir();
	}

	/**
	 * Method for export an attache mesh to object
	 * 
	 * @param destinationFile
	 * @param directoryName
	 * @param attacheMesh
	 */
	static public void exportAttacheMeshToObject(String destinationFile, String directoryName, Mesh attacheMesh) {
		File file = new File(destinationFile + "\\" + directoryName + "\\" + "Attache.obj");
		try (FileWriter fileWriter = new FileWriter(file)) {
			fileWriter.write("# Fichier réalisé par\n");
			fileWriter.write("# Alexis Dardinier\n");
			fileWriter.write("# Thomas Klein\n");
			fileWriter.write("# Pierre Petit\n");
			fileWriter.write("# Timothé Rouzé\n");
			fileWriter.write("# Mathieu Vincent\n\n");
			fileWriter.write("#Pièce à imprimer "
					+ getNumberOfClip(Cut.getWidthCutNumber(), Cut.getHeightCutNumber()).toString() + " fois\n");
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
			System.out.println("attache exporté dans");
			System.out.println(file.getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
