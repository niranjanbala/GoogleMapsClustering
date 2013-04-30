package open.source.google.map.clustering.model;

import open.source.google.map.clustering.util.Constants;
import open.source.google.map.clustering.util.MathUtil;

public class Point implements IPoint {

	private double x;
	private double y;
	private int countCluster;
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
		this.countCluster = 1;
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

	public void normalize() {
		this.y = MathUtil.normalizeLatitude(y);
		this.x = MathUtil.normalizeLongitude(x);
	}

	// / <summary>
	// / Lat Lon specific rect boundary check, is x,y inside boundary?
	// / </summary>
	// / <param name="minx"></param>
	// / <param name="miny"></param>
	// / <param name="maxx"></param>
	// / <param name="maxy"></param>
	// / <param name="x"></param>
	// / <param name="y"></param>
	// / /// <param name="isInsideDetectedX"></param>
	// / /// <param name="isInsideDetectedY"></param>
	// / <returns></returns>
	public boolean isInside(Boundary boundary, boolean isInsideDetectedX,
			boolean isInsideDetectedY) {
		// Normalize because of widen function, world wrapping might have
		// occured
		// calc in positive value range only, nb. lon -170 = 10, lat -80 = 10
		boundary.normalize();
		this.normalize();
		double nminx = boundary.getMinx();
		double nmaxx = boundary.getMaxx();

		double nminy = boundary.getMiny();
		double nmaxy = boundary.getMaxy();

		double nx = x;
		double ny = y;

		boolean isX = isInsideDetectedX; // skip checking?
		boolean isY = isInsideDetectedY;

		if (!isInsideDetectedY) {
			// world wrap y
			if (nminy > nmaxy) {
				// sign depended check, todo merge equal lines
				// - -
				if (nmaxy <= 0 && nminy <= 0) {
					isY = nminy <= ny && ny <= Constants.MAX_LAT_VALUE
							|| Constants.MIN_LAT_VALUE <= ny && ny <= nmaxy;
				}
				// + +
				else if (nmaxy >= 0 && nminy >= 0) {
					isY = nminy <= ny && ny <= Constants.MAX_LAT_VALUE
							|| Constants.MIN_LAT_VALUE <= ny && ny <= nmaxy;
				}
				// + -
				else {
					isY = nminy <= ny && ny <= Constants.MAX_LAT_VALUE
							|| Constants.MIN_LAT_VALUE <= ny && ny <= nmaxy;
				}
			}

			else {
				// normal, no world wrap
				isY = nminy <= ny && ny <= nmaxy;
			}
		}

		if (!isInsideDetectedX) {
			// world wrap x
			if (nminx > nmaxx) {
				// sign depended check, todo merge equal lines
				// - -
				if (nmaxx <= 0 && nminx <= 0) {
					isX = nminx <= nx && nx <= Constants.MAX_LON_VALUE
							|| Constants.MIN_LON_VALUE <= nx && nx <= nmaxx;
				}
				// + +
				else if (nmaxx >= 0 && nminx >= 0) {
					isX = nminx <= nx && nx <= Constants.MAX_LON_VALUE
							|| Constants.MIN_LON_VALUE <= nx && nx <= nmaxx;
				}
				// + -
				else {
					isX = nminx <= nx && nx <= Constants.MAX_LON_VALUE
							|| Constants.MIN_LON_VALUE <= nx && nx <= nmaxx;
				}
			} else {
				// normal, no world wrap
				isX = nminx <= nx && nx <= nmaxx;
			}
		}

		return isX && isY;
	}

	public boolean isInside(Boundary b) {
		return isInside(b, false, false);
	}

}
