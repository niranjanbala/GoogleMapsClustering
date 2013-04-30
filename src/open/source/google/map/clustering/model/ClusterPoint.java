package open.source.google.map.clustering.model;

import open.source.google.map.clustering.util.Constants;
import open.source.google.map.clustering.util.MathUtil;

public abstract class ClusterPoint implements IPoint {

	private int countCluster;

	public ClusterPoint() {
		countCluster = 1;
	}

	public int getCountCluster() {
		return countCluster;
	}

	public void setCountCluster(int countCluster) {
		this.countCluster = countCluster;
	}

	public final boolean isClusterPoint() {
		return this.countCluster > 1;
	}

	public final void normalize() {
		this.setY(MathUtil.normalizeLatitude(this.getY()));
		this.setX(MathUtil.normalizeLongitude(this.getX()));
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
	public final boolean isInside(Boundary boundary, boolean isInsideDetectedX,
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

		double nx = this.getX();
		double ny = this.getY();

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

	public final boolean isInside(Boundary b) {
		return isInside(b, false, false);
	}

}
