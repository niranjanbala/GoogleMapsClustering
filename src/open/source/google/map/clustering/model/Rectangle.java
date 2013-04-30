package open.source.google.map.clustering.model;

public class Rectangle {
	protected double minx;
	protected double maxx;
	protected double miny ;
	protected double maxy ;    
	public Rectangle(double minx, double maxx, double miny, double maxy) {
		super();
		this.minx = minx;
		this.maxx = maxx;
		this.miny = miny;
		this.maxy = maxy;
	}
	public double getMinx() {
		return minx;
	}
	public double getMaxx() {
		return maxx;
	}
	public double getMiny() {
		return miny;
	}
	public double getMaxy() {
		return maxy;
	}
}
