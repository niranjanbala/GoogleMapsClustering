package open.source.google.map.clustering.model;

public class Point {
	private double latitude;
	private double longtitude;
	private double x;
	private double y;
	private int countCluster;
	private int markerId;
	private int markerType;

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongtitude() {
		return longtitude;
	}

	public void setLongtitude(double longtitude) {
		this.longtitude = longtitude;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public int getCountCluster() {
		return countCluster;
	}

	public void setCountCluster(int countCluster) {
		this.countCluster = countCluster;
	}

	public int getMarkerId() {
		return markerId;
	}

	public void setMarkerId(int markerId) {
		this.markerId = markerId;
	}

	public int getMarkerType() {
		return markerType;
	}

	public void setMarkerType(int markerType) {
		this.markerType = markerType;
	}

}
