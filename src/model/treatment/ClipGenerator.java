package model.treatment;

import java.awt.image.BufferedImage;

import model.mesh.MapMesh;

public class ClipGenerator {

	
	/**
	 * Method to generate clip
	 * 
	 * @param bufferedImageParcel
	 * @return Clip structure 3___4 11___12 | |5__________7| | | | |
	 *         6__________8 | |___| |___| 1 2 9 10 Vertices s0X: vertices at the
	 *         top of verticeX
	 */
	public MapMesh clipGenerator(BufferedImage bufferedImageParcel) {

		/*
		 * double deb = bufferedImageParcel.getWidth() * 0.1; MapMesh clipMesh =
		 * new MapMesh(bufferedImageParcel.getHeight(),
		 * bufferedImageParcel.getWidth()); Vertices vertices1 = new Vertices(0,
		 * 0, 0); clipMesh.getSetOfVertices().put(vertices1.getId(), vertices1);
		 * Vertices vertices01 = new Vertices(0, 3, 0);
		 * clipMesh.getSetOfVertices().put(vertices01.getId(), vertices01);
		 * Vertices vertices2 = new Vertices(deb / 2, 0, 0);
		 * clipMesh.getSetOfVertices().put(vertices2.getId(), vertices2);
		 * Vertices vertices02 = new Vertices(deb / 2, 3, 0);
		 * clipMesh.getSetOfVertices().put(vertices02.getId(), vertices02);
		 * Vertices vertices3 = new Vertices(0, 0, 2 * deb);
		 * clipMesh.getSetOfVertices().put(vertices3.getId(), vertices3);
		 * Vertices vertices03 = new Vertices(0, 3, 2 * deb);
		 * clipMesh.getSetOfVertices().put(vertices03.getId(), vertices03);
		 * Vertices vertices4 = new Vertices(deb / 2, 0, 2 * deb);
		 * clipMesh.getSetOfVertices().put(vertices4.getId(), vertices4);
		 * Vertices vertices04 = new Vertices(deb / 2, 3, 2 * deb);
		 * clipMesh.getSetOfVertices().put(vertices04.getId(), vertices04);
		 * Vertices vertices5 = new Vertices(deb / 2, 0, 1.5 * deb);
		 * clipMesh.getSetOfVertices().put(vertices5.getId(), vertices5);
		 * Vertices vertices05 = new Vertices(deb / 2, 3, 1.5 * deb);
		 * clipMesh.getSetOfVertices().put(vertices05.getId(), vertices05);
		 * Vertices vertices6 = new Vertices(deb / 2, 0, deb / 2);
		 * clipMesh.getSetOfVertices().put(vertices6.getId(), vertices6);
		 * Vertices vertices06 = new Vertices(deb / 2, 3, deb / 2);
		 * clipMesh.getSetOfVertices().put(vertices06.getId(), vertices06);
		 * Vertices vertices7 = new Vertices(2.5 * deb, 0, 1.5 * deb);
		 * clipMesh.getSetOfVertices().put(vertices7.getId(), vertices7);
		 * Vertices vertices07 = new Vertices(2.5 * deb, 3, 1.5 * deb);
		 * clipMesh.getSetOfVertices().put(vertices07.getId(), vertices07);
		 * Vertices vertices8 = new Vertices(2.5 * deb, 0, deb / 2);
		 * clipMesh.getSetOfVertices().put(vertices8.getId(), vertices8);
		 * Vertices vertices08 = new Vertices(2.5 * deb, 3, deb / 2);
		 * clipMesh.getSetOfVertices().put(vertices08.getId(), vertices08);
		 * Vertices vertices9 = new Vertices(2.5 * deb, 0, 0);
		 * clipMesh.getSetOfVertices().put(vertices9.getId(), vertices9);
		 * Vertices vertices09 = new Vertices(2.5 * deb, 3, 0);
		 * clipMesh.getSetOfVertices().put(vertices09.getId(), vertices09);
		 * Vertices vertices10 = new Vertices(3 * deb, 0, 0);
		 * clipMesh.getSetOfVertices().put(vertices10.getId(), vertices10);
		 * Vertices vertices010 = new Vertices(3 * deb, 3, 0);
		 * clipMesh.getSetOfVertices().put(vertices010.getId(), vertices010);
		 * Vertices vertices11 = new Vertices(2.5 * deb, 0, 2 * deb);
		 * clipMesh.getSetOfVertices().put(vertices11.getId(), vertices11);
		 * Vertices vertices011 = new Vertices(2.5 * deb, 3, 2 * deb);
		 * clipMesh.getSetOfVertices().put(vertices011.getId(), vertices011);
		 * Vertices vertices12 = new Vertices(3 * deb, 0, 2 * deb);
		 * clipMesh.getSetOfVertices().put(vertices12.getId(), vertices12);
		 * Vertices vertices012 = new Vertices(3 * deb, 3, 2 * deb);
		 * clipMesh.getSetOfVertices().put(vertices012.getId(), vertices012);
		 * 
		 * // faces horizontales clipMesh.getSetOfFaces().add(new
		 * Face(vertices1.getId(), vertices2.getId(), vertices3.getId()));
		 * clipMesh.getSetOfFaces().add(new Face(vertices01.getId(),
		 * vertices02.getId(), vertices03.getId()));
		 * clipMesh.getSetOfFaces().add(new Face(vertices2.getId(),
		 * vertices3.getId(), vertices4.getId()));
		 * clipMesh.getSetOfFaces().add(new Face(vertices02.getId(),
		 * vertices03.getId(), vertices04.getId()));
		 * clipMesh.getSetOfFaces().add(new Face(vertices5.getId(),
		 * vertices6.getId(), vertices7.getId()));
		 * clipMesh.getSetOfFaces().add(new Face(vertices05.getId(),
		 * vertices06.getId(), vertices07.getId()));
		 * clipMesh.getSetOfFaces().add(new Face(vertices6.getId(),
		 * vertices7.getId(), vertices8.getId()));
		 * clipMesh.getSetOfFaces().add(new Face(vertices06.getId(),
		 * vertices07.getId(), vertices08.getId()));
		 * clipMesh.getSetOfFaces().add(new Face(vertices9.getId(),
		 * vertices10.getId(), vertices11.getId()));
		 * clipMesh.getSetOfFaces().add(new Face(vertices09.getId(),
		 * vertices010.getId(), vertices011.getId()));
		 * clipMesh.getSetOfFaces().add(new Face(vertices10.getId(),
		 * vertices11.getId(), vertices12.getId()));
		 * clipMesh.getSetOfFaces().add(new Face(vertices010.getId(),
		 * vertices011.getId(), vertices012.getId()));
		 * 
		 * // faces verticales
		 * 
		 * clipMesh.getSetOfFaces().add(new Face(vertices1.getId(),
		 * vertices01.getId(), vertices3.getId()));
		 * clipMesh.getSetOfFaces().add(new Face(vertices3.getId(),
		 * vertices03.getId(), vertices01.getId()));
		 * 
		 * clipMesh.getSetOfFaces().add(new Face(vertices1.getId(),
		 * vertices01.getId(), vertices2.getId()));
		 * clipMesh.getSetOfFaces().add(new Face(vertices01.getId(),
		 * vertices02.getId(), vertices2.getId()));
		 * 
		 * clipMesh.getSetOfFaces().add(new Face(vertices3.getId(),
		 * vertices03.getId(), vertices4.getId()));
		 * clipMesh.getSetOfFaces().add(new Face(vertices03.getId(),
		 * vertices04.getId(), vertices4.getId()));
		 * 
		 * clipMesh.getSetOfFaces().add(new Face(vertices2.getId(),
		 * vertices02.getId(), vertices6.getId()));
		 * clipMesh.getSetOfFaces().add(new Face(vertices02.getId(),
		 * vertices06.getId(), vertices6.getId()));
		 * 
		 * clipMesh.getSetOfFaces().add(new Face(vertices4.getId(),
		 * vertices04.getId(), vertices5.getId()));
		 * clipMesh.getSetOfFaces().add(new Face(vertices04.getId(),
		 * vertices05.getId(), vertices5.getId()));
		 * 
		 * clipMesh.getSetOfFaces().add(new Face(vertices6.getId(),
		 * vertices06.getId(), vertices8.getId()));
		 * clipMesh.getSetOfFaces().add(new Face(vertices06.getId(),
		 * vertices08.getId(), vertices8.getId()));
		 * 
		 * clipMesh.getSetOfFaces().add(new Face(vertices5.getId(),
		 * vertices05.getId(), vertices7.getId()));
		 * clipMesh.getSetOfFaces().add(new Face(vertices05.getId(),
		 * vertices07.getId(), vertices7.getId()));
		 * 
		 * clipMesh.getSetOfFaces().add(new Face(vertices8.getId(),
		 * vertices9.getId(), vertices08.getId()));
		 * clipMesh.getSetOfFaces().add(new Face(vertices08.getId(),
		 * vertices9.getId(), vertices09.getId()));
		 * 
		 * clipMesh.getSetOfFaces().add(new Face(vertices7.getId(),
		 * vertices07.getId(), vertices11.getId()));
		 * clipMesh.getSetOfFaces().add(new Face(vertices07.getId(),
		 * vertices11.getId(), vertices011.getId()));
		 * 
		 * clipMesh.getSetOfFaces().add(new Face(vertices9.getId(),
		 * vertices10.getId(), vertices09.getId()));
		 * clipMesh.getSetOfFaces().add(new Face(vertices09.getId(),
		 * vertices10.getId(), vertices010.getId()));
		 * 
		 * clipMesh.getSetOfFaces().add(new Face(vertices11.getId(),
		 * vertices12.getId(), vertices011.getId()));
		 * clipMesh.getSetOfFaces().add(new Face(vertices011.getId(),
		 * vertices012.getId(), vertices12.getId()));
		 * 
		 * clipMesh.getSetOfFaces().add(new Face(vertices12.getId(),
		 * vertices10.getId(), vertices012.getId()));
		 * clipMesh.getSetOfFaces().add(new Face(vertices10.getId(),
		 * vertices012.getId(), vertices010.getId()));
		 * 
		 * return clipMesh;
		 */
		return null;
	}
}
