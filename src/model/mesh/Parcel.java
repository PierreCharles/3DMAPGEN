package model.mesh;

import config.Config;

public class Parcel {

	private MapMesh mapMesh;
	private String parcelName;
	private int partelID;
	private static int Partel_Counter = 1;
	
	public Parcel(MapMesh mapMesh){
		this.mapMesh = mapMesh;
		partelID = Partel_Counter;
		this.parcelName = new String(Config.EXPORT_FILE_NAME+partelID);
		Partel_Counter++;
	}
	
	public String getParcelName() {
		return parcelName;
	}
	
	public int getPartelID(){
		return partelID;
	}

	public void setParcelName(String parcelName) {
		this.parcelName = parcelName;
	}

	public MapMesh getMapMesh() {
		return mapMesh;
	}

	@Override
	public String toString() {
		return parcelName;
	}	

}
