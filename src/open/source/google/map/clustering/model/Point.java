package open.source.google.map.clustering.model;

public class Point {

	private double x;
	private double y;
	private int countCluster;
	private int markerId;
	private int markerType;

	public Point() {

	}

	public Point(double x, double y, int countCluster, int markerId,
			int markerType) {
		super();
		this.x = x;
		this.y = y;
		this.countCluster = countCluster;
		this.markerId = markerId;
		this.markerType = markerType;
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
