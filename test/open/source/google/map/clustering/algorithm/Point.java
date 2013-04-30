package open.source.google.map.clustering.algorithm;

import open.source.google.map.clustering.model.ClusterPoint;

public class Point extends ClusterPoint {

	private double x;
	private double y;
	private int markerId;
	private int markerType;
	private String displayText;

	public String getDisplayText() {
		return displayText;
	}

	public void setDisplayText(String displayText) {
		this.displayText = displayText;
	}

	public Point(double x, double y, int markerId, int markerType) {
		super();
		this.x = x;
		this.y = y;
		this.markerId = markerId;
		this.markerType = markerType;
		this.displayText = "";
	}

	@Override
	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	@Override
	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
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
