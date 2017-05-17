package mesh;

public class Vertice {
	
    private static int counter = 1;

    private double x;
	private double y;
    private double z;
    private int id;

    /**
     * Vertice constructor
     * @param line
     * @param hieght
     * @param column
     */
    public Vertice(double line, double hieght, double column) {
        x = column;
        y = hieght;
        z = line;
        id = counter;
        counter++;
    }
    
    /**
     * Getter of x value
     * @return x value
     */
    public double getX() {
		return x;
	}

    /**
     * Setter of x value
     * @param new x value
     */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * Getter of y value
	 * @return y value
	 */
	public double getY() {
		return y;
	}

	/**
	 * Setter of y value
	 * @param new y value
	 */
	public void setY(double y) {
		this.y = y;
	}


	/**
	 * Getter of z value
	 * @return z value
	 */
	public double getZ() {
		return z;
	}


	/**
	 * Setter of z value
	 * @param new z value
	 */
	public void setZ(double z) {
		this.z = z;
	}


	/**
	 * Getter of id vertice
	 * @return vertice id
	 */
	public int getId() {
		return id;
	}


	/**
	 * Setter of vertice id
	 * @param id of vertice
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Method for reset the counter
	 */
    public static void resetCpt() {
    	counter = 1;
    }
    
    /**
     * Method for get if an another vertice is equal
     * @param the another vertice
     * @return a boolean : true if equal else false
     */
    public boolean equals(Vertice s) {
        return (this==s);
    }
    
    /**
     * ToString vertice method
     * @return a string
     */
    @Override
    public String toString() {
        return "v " + this.x + " " + this.y + " " + this.z + "\r\n";
    }
    
    /**
     * Method for move on X 
     * @param distance to move
     */
    public void moveX(double distance) {
        setX(getX() + distance);
    }
    
    /**
     * Method for move on Y
     * @param distance to move
     */
    public void moveY(double distance) {
        setY(getY() + distance);
    }
    
    /**
     * Method for move on Z 
     * @param distance to move
     */
    public void moveZ(double distance) {
        setZ(getZ() + distance);
    }
}
