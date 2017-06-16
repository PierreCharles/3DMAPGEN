package model.mesh;

import wblut.geom.WB_Coord;

public class Point3D implements WB_Coord {

	private static int WB_COORD_3D_COUNTER = 0 ;
	private final static int WB_COORD_3D_DEFAULT_COUNTER = 0; 
	
	private double x, y, z;
	private int id;
	
	public Point3D(double xf, double yf, double zf) {
		super();
		this.x = xf;
		this.y = yf;
		this.z = zf;
		this.id = WB_COORD_3D_COUNTER++;
	}
	
	public void resetCounter(){
		WB_COORD_3D_COUNTER = WB_COORD_3D_DEFAULT_COUNTER;
	}
	
	public int getId(){
		return this.id;
	}
	
	@Override
	public double xd() {
		return x;
	}

	@Override
	public double yd() {
		return y;
	}

	@Override
	public double zd() {
		return z;
	}
	
	@Override
	public float xf() {
		return (float) x;
	}

	@Override
	public float yf() {
		return (float) y;
	}

	@Override
	public float zf() {
		return (float) z;
	}

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
