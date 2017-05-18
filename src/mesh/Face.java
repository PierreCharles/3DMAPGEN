package mesh;

/**
 * Class Face The face that will be composed of three points will be written
 * like this: f 1 2 3. To write the OBJ files, we instantiate the faces with f
 * followed by the number of the lines of the desired points.
 * 
 * @author
 */
public class Face {

	public static int counter = 1;
	private int id = 0;
	private int idVertice1 = 0;
	private int idVertice2 = 0;
	private int idVertice3 = 0;

	/**
	 * Constructor of a face
	 * 
	 * @param idVertice1
	 * @param idVertice2
	 * @param idVertice3
	 */
	public Face(int idVertice1, int idVertice2, int idVertice3) {
		this.id = counter;
		counter++;
		this.idVertice1 = idVertice1;
		this.idVertice2 = idVertice2;
		this.idVertice3 = idVertice3;
	}

	/**
	 * Getter of face id
	 * 
	 * @return id of face
	 */
	public int getId() {
		return id;
	}

	/**
	 * Setter of face id
	 * 
	 * @param id of current face
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Getter of first vertice id
	 * 
	 * @return first vertice id
	 */
	public int getIdVertice1() {
		return idVertice1;
	}

	/**
	 * Setter of first vertice id
	 * 
	 * @param idVertice1
	 */
	public void setIdVertice1(int idVertice1) {
		this.idVertice1 = idVertice1;
	}

	/**
	 * Getter of second vertice id
	 * 
	 * @return second vertice id
	 */
	public int getIdVertice2() {
		return idVertice2;
	}

	/**
	 * Setter of second vertice id
	 * 
	 * @param idVertice2
	 */
	public void setIdVertice2(int idVertice2) {
		this.idVertice2 = idVertice2;
	}

	/**
	 * Getter of third vertice id
	 * 
	 * @return third vertice id
	 */
	public int getIdVertice3() {
		return idVertice3;
	}

	/**
	 * Setter of third vertice id
	 * 
	 * @param idVertice3
	 */
	public void setIdVertice3(int idVertice3) {
		this.idVertice3 = idVertice3;
	}

	/**
	 * ToString method of a face object
	 * 
	 * @return a string
	 */
	@Override
	public String toString() {
		return "f " + this.idVertice1 + " " + this.idVertice2 + " " + this.idVertice3 + "\r\n";
	}

}
