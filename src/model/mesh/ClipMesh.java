package model.mesh;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import config.Config;
import model.treatment.MapGenerator;

public class ClipMesh {
	
	
	/**
	 * Method for export an attache mesh to object
	 * 
	 * @param destinationFile
	 * @param directoryName
	 * @param attacheMesh
	 */
	/*
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
	*/
	
}
