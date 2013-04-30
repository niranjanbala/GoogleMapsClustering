package open.source.google.map.clustering.model;

import java.util.ArrayList;
import java.util.List;

public class Bucket {
	private boolean isUsed;
	private String id;
	private List<ClusterPoint> points;
	int x;
	int y;
	private ClusterPoint centroid;
	private double errorLevel;

	public boolean isUsed() {
		return isUsed;
	}

	public void setUsed(boolean isUsed) {
		this.isUsed = isUsed;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<ClusterPoint> getPoints() {
		return points;
	}

	public void setPoints(List<ClusterPoint> points) {
		this.points = points;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public ClusterPoint getCentroid() {
		return centroid;
	}

	public void setCentroid(ClusterPoint centroid) {
		this.centroid = centroid;
	}

	public double getErrorLevel() {
		return errorLevel;
	}

	public void setErrorLevel(double errorLevel) {
		this.errorLevel = errorLevel;
	}

	public Bucket(String id) {
		this.isUsed = true;
		this.centroid = null;
		this.points = new ArrayList<ClusterPoint>();
		this.id = id;
	}

	public Bucket(int x, int y, String id) {
		this.isUsed = true;
		this.centroid = null;
		this.points = new ArrayList<ClusterPoint>();
		this.x = x;
		this.y = y;
		this.id = id;
	}

}
