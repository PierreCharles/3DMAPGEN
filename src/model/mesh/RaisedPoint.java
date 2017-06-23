package model.mesh;

import java.util.ArrayList;

public class RaisedPoint {
	
	public static final ArrayList<RaisedPoint> RAISED_LIST_POINTS = generateRaisedPoint();

	int x, y;

	public RaisedPoint(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	@Override
	public boolean equals(Object raisedPointObject) {
		RaisedPoint raisedPoint = (RaisedPoint) raisedPointObject;
		if (raisedPoint.x == x && raisedPoint.y == y)
			return true;
		return false;
	}
	
	
	/**
	 * Methode to set the raised point size
	 */
	private static ArrayList<RaisedPoint> generateRaisedPoint() {
		
		ArrayList<RaisedPoint> raisedListPoint = new ArrayList<RaisedPoint>();
		
		// Top clip points
		raisedListPoint.add(new RaisedPoint(0,5));
		raisedListPoint.add(new RaisedPoint(0,6));
		raisedListPoint.add(new RaisedPoint(1,4));
		raisedListPoint.add(new RaisedPoint(1,5));
		raisedListPoint.add(new RaisedPoint(1,6));
		raisedListPoint.add(new RaisedPoint(1,7));
		raisedListPoint.add(new RaisedPoint(2,4));
		raisedListPoint.add(new RaisedPoint(2,5));
		raisedListPoint.add(new RaisedPoint(2,6));
		raisedListPoint.add(new RaisedPoint(2,7));
		
		// Left clip points
		raisedListPoint.add(new RaisedPoint(4,1));
		raisedListPoint.add(new RaisedPoint(4,2));
		raisedListPoint.add(new RaisedPoint(5,0));
		raisedListPoint.add(new RaisedPoint(5,1));
		raisedListPoint.add(new RaisedPoint(5,2));
		raisedListPoint.add(new RaisedPoint(6,0));
		raisedListPoint.add(new RaisedPoint(6,1));
		raisedListPoint.add(new RaisedPoint(6,2));
		raisedListPoint.add(new RaisedPoint(7,1));
		raisedListPoint.add(new RaisedPoint(7,2));
		
		// Right clip points
		raisedListPoint.add(new RaisedPoint(4,9));
		raisedListPoint.add(new RaisedPoint(4,10));
		raisedListPoint.add(new RaisedPoint(5,9));
		raisedListPoint.add(new RaisedPoint(5,10));
		raisedListPoint.add(new RaisedPoint(5,11));
		raisedListPoint.add(new RaisedPoint(6,9));
		raisedListPoint.add(new RaisedPoint(6,10));
		raisedListPoint.add(new RaisedPoint(6,11));
		raisedListPoint.add(new RaisedPoint(7,9));
		raisedListPoint.add(new RaisedPoint(7,10));
		
		// Bottom clip points
		raisedListPoint.add(new RaisedPoint(9,4));
		raisedListPoint.add(new RaisedPoint(9,5));
		raisedListPoint.add(new RaisedPoint(9,6));
		raisedListPoint.add(new RaisedPoint(9,7));
		raisedListPoint.add(new RaisedPoint(10,4));
		raisedListPoint.add(new RaisedPoint(10,5));
		raisedListPoint.add(new RaisedPoint(10,6));
		raisedListPoint.add(new RaisedPoint(10,7));
		raisedListPoint.add(new RaisedPoint(11,5));
		raisedListPoint.add(new RaisedPoint(11,6));
		
		//Center clip points
		raisedListPoint.add(new RaisedPoint(3,3));
		raisedListPoint.add(new RaisedPoint(3,4));
		raisedListPoint.add(new RaisedPoint(3,5));
		raisedListPoint.add(new RaisedPoint(3,6));
		raisedListPoint.add(new RaisedPoint(3,7));
		raisedListPoint.add(new RaisedPoint(3,8));
		raisedListPoint.add(new RaisedPoint(4,3));
		raisedListPoint.add(new RaisedPoint(4,4));
		raisedListPoint.add(new RaisedPoint(4,5));
		raisedListPoint.add(new RaisedPoint(4,6));
		raisedListPoint.add(new RaisedPoint(4,7));
		raisedListPoint.add(new RaisedPoint(4,8));
		raisedListPoint.add(new RaisedPoint(5,3));
		raisedListPoint.add(new RaisedPoint(5,4));
		raisedListPoint.add(new RaisedPoint(5,5));
		raisedListPoint.add(new RaisedPoint(5,6));
		raisedListPoint.add(new RaisedPoint(5,7));
		raisedListPoint.add(new RaisedPoint(5,8));
		raisedListPoint.add(new RaisedPoint(6,3));
		raisedListPoint.add(new RaisedPoint(6,4));
		raisedListPoint.add(new RaisedPoint(6,5));
		raisedListPoint.add(new RaisedPoint(6,6));
		raisedListPoint.add(new RaisedPoint(6,7));
		raisedListPoint.add(new RaisedPoint(6,8));
		raisedListPoint.add(new RaisedPoint(7,3));
		raisedListPoint.add(new RaisedPoint(7,4));
		raisedListPoint.add(new RaisedPoint(7,5));
		raisedListPoint.add(new RaisedPoint(7,6));
		raisedListPoint.add(new RaisedPoint(7,7));
		raisedListPoint.add(new RaisedPoint(7,8));
		raisedListPoint.add(new RaisedPoint(8,3));
		raisedListPoint.add(new RaisedPoint(8,4));
		raisedListPoint.add(new RaisedPoint(8,5));
		raisedListPoint.add(new RaisedPoint(8,6));
		raisedListPoint.add(new RaisedPoint(8,7));
		raisedListPoint.add(new RaisedPoint(8,8));
		
		return raisedListPoint;
	}
}
