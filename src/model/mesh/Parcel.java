package model.mesh;

public class Parcel {

	private MapMesh mapMesh;
	private String parcelName;
	
	public Parcel(MapMesh mapMesh){
		this.mapMesh = mapMesh;
		this.parcelName = "Test Name";
	}
	
	public String getParcelName() {
		return parcelName;
	}

	public void setParcelName(String parcelName) {
		this.parcelName = parcelName;
	}

	public MapMesh getMapMesh() {
		return mapMesh;
	}	

}
