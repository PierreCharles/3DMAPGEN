package mesh;

/**
 * Vertices class A vertices is defined as : v X Y Z // Pour un point de coordonnées (X;Y;Z)
 * 
 * @author
 */
public class Vertices {

	private static int counter = 1;

	private double x;
	private double y;
	private double z;
	private int id;

	/**
	 * Vertices constructor
	 * 
	 * @param line
	 * @param hieght
	 * @param column
	 */
	public Vertices(double line, double hieght, double column) {
		x = column;
		y = hieght;
		z = line;
		id = counter;
		counter++;
	}

	/**
	 * Getter of x value
	 * 
	 * @return x value
	 */
	public double getX() {
		return x;
	}

	/**
	 * Setter of x value
	 * 
	 * @param new : x value
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * Getter of y value
	 * 
	 * @return y value
	 */
	public double getY() {
		return y;
	}

	/**
	 * Setter of y value
	 * 
	 * @param new
	 *            y value
	 */
	public void setY(double y) {
		this.y = y;
	}

	/**
	 * Getter of z value
	 * 
	 * @return z value
	 */
	public double getZ() {
		return z;
	}

	/**
	 * Setter of z value
	 * 
	 * @param new
	 *            z value
	 */
	public void setZ(double z) {
		this.z = z;
	}

	/**
	 * Getter of id vertices
	 * 
	 * @return vertices id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Setter of vertices id
	 * 
	 * @param id : of vertices
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Method for reset the counter
	 */
	public static void resetCounter() {
		counter = 1;
	}

	/**
	 * Method for get if an another vertices is equal
	 * 
	 * @param the : another vertices
	 * @return a boolean : true if equal else false
	 */
	public boolean equals(Vertices vertices) {
		return (this == vertices);
	}

	/**
	 * ToString vertices method
	 * 
	 * @return a string
	 */
	@Override
	public String toString() {
		return "v " + this.x + " " + this.y + " " + this.z + "\r\n";
	}

	/**
	 * Method for move on X
	 * 
	 * @param distance : to move
	 */
	public void moveX(double distance) {
		setX(getX() + distance);
	}

	/**
	 * Method for move on Y
	 * 
	 * @param distance : to move
	 */
	public void moveY(double distance) {
		setY(getY() + distance);
	}

	/**
	 * Method for move on Z
	 * 
	 * @param distance : to move
	 */
	public void moveZ(double distance) {
		setZ(getZ() + distance);
	}
}
