package model.mesh;

import wblut.geom.WB_Coord;

/**
 * Custom Point3D class implement WB_Coord of the HE_Mesh library
 * 
 * @author picharles
 *
 */
public class Point3D implements WB_Coord {

	private double x, y, z;
	
	@Override
	public String toString() {
		return "Point3D [x=" + x + ", y=" + y + ", z=" + z + "]";
	}

	/**
	 * Constructor of 3DPoint
	 * 
	 * @param xf : double
	 * @param yf : double
	 * @param zf : double
	 */
	public Point3D(double xf, double yf, double zf) {
		super();
		this.x = xf;
		this.y = yf;
		this.z = zf;
	}

	/**
	 * Method to compare this Coord to another Coord
	 */
	@Override
	public int compareTo(WB_Coord p) {
		int cmp = Double.compare(xd(), p.xd());
		if (cmp != 0) {
			return cmp;
		}
		cmp = Double.compare(yd(), p.yd());
		if (cmp != 0) {
			return cmp;
		}
		cmp = Double.compare(zd(), p.zd());
		if (cmp != 0) {
			return cmp;
		}
		return Double.compare(wd(), p.wd());
	}

	/**
	 * Method to get the double value of the a requested axe of the point
	 */
	@Override
	public double getd(int i) {
		if (i == 0) {
			return x;
		}
		if (i == 1) {
			return y;
		}
		if (i == 2) {
			return 0;
		}
		return Double.NaN;
	}

	/**
	 * Method to get the float value of the a requested axe of the point
	 */
	@Override
	public float getf(int i) {
		if (i == 0) {
			return (float) x;
		}
		if (i == 1) {
			return (float) y;
		}
		if (i == 2) {
			return (float) z;
		}
		return Float.NaN;
	}
	
	/**
	 * Override getter of x type double
	 */
	@Override
	public double xd() {
		return x;
	}

	/**
	 * Override getter of y type double
	 */
	@Override
	public double yd() {
		return y;
	}

	/**
	 * Override getter of z type double
	 */
	@Override
	public double zd() {
		return z;
	}
	
	/**
	 * Override getter of x type float
	 */
	@Override
	public float xf() {
		return (float) x;
	}

	/**
	 * Override getter of y type float
	 */
	@Override
	public float yf() {
		return (float) y;
	}

	/**
	 * Override getter of z type float
	 */
	@Override
	public float zf() {
		return (float) z;
	}
	
	
	@Override
	public float wf() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public double wd() {
		// TODO Auto-generated method stub
		return 0;
	}

}
