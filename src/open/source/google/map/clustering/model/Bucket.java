package open.source.google.map.clustering.model;

import java.util.ArrayList;
import java.util.List;

public class Bucket {
	private boolean isUsed;
	private String id;
	private List<Point> points;
	int x;
	int y;
	private Point centroid;
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

	public List<Point> getPoints() {
		return points;
	}

	public void setPoints(List<Point> points) {
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

	public Point getCentroid() {
		return centroid;
	}

	public void setCentroid(Point centroid) {
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
		this.points = new ArrayList<Point>();
		this.id = id;
	}

	public Bucket(int x, int y, String id) {
		this.isUsed = true;
		this.centroid = null;
		this.points = new ArrayList<Point>();
		this.x = x;
		this.y = y;
		this.id = id;
	}

}
