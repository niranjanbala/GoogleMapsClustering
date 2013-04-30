package open.source.google.map.clustering.util;

import open.source.google.map.clustering.model.Boundary;
import open.source.google.map.clustering.model.ClusterConfiguration;
import open.source.google.map.clustering.model.Point;

public class LocationUtil {

	// Don't filter when zoomed far out, because user json receive swlon and
	// nelon values "jumps" to next overlapping lon point
	// and the program can't know from what to filter.
	// e.g. Is it from 160 to -160 or 160 to -160+360 ?? lon value 10 is
	// filtered out in first case but should be included in 2nd case
	// heuristic and is based on html width value

	// Set a higher zoomlevel value if you increase the html window size. This
	// works with width 800px, height 600px
	public static boolean canFilterData(int zoomLevel) {
		return zoomLevel >= 3;
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
	public static boolean IsInside(double minx, double miny, double maxx,
			double maxy, double x, double y, boolean isInsideDetectedX,
			boolean isInsideDetectedY) {
		// Normalize because of widen function, world wrapping might have
		// occured
		// calc in positive value range only, nb. lon -170 = 10, lat -80 = 10
		double nminx = normalizeLongitude(minx);
		double nmaxx = normalizeLongitude(maxx);

		double nminy = normalizeLatitude(miny);
		double nmaxy = normalizeLatitude(maxy);

		double nx = normalizeLongitude(x);
		double ny = normalizeLatitude(y);

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

	public static boolean IsInside(Boundary b, Point p) {
		return IsInside(b.getMinx(), b.getMiny(), b.getMaxx(), b.getMaxy(),
				p.getX(), p.getY(), false, false);
	}

	// ]-180;180]
	// lon wrap around at value -180 and 180, nb. -180 = 180
	public static double normalizeLongitude(double lon) {
		// naive version
		// while(normalized<MinValue) ... normalized += MaxValue;
		double normalized = lon;
		if (lon < Constants.MIN_LON_VALUE) {
			double m = lon % Constants.MIN_LON_VALUE;
			normalized = Constants.MAX_LON_VALUE + m;
		} else if (lon > Constants.MAX_LON_VALUE) {
			double m = lon % Constants.MAX_LON_VALUE;
			normalized = Constants.MIN_LON_VALUE + m;
		}

		return normalized;
	}

	// [-90;90]
	// -90 is south pole, 90 is north pole thus -90 != 90
	// no wrap, because google map dont wrap on lat
	public static double normalizeLatitude(double lat) {
		double normalized = lat;
		if (lat < Constants.MIN_LAT_VALUE) {
			// var m = lat % -LatLonInfo.MaxLatValue;
			// normalized = LatLonInfo.MaxLatValue + m;
			normalized = Constants.MIN_LAT_VALUE;
		}
		if (lat > Constants.MAX_LAT_VALUE) {
			// var m = lat % LatLonInfo.MaxLatValue;
			// normalized = -LatLonInfo.MaxLatValue + m;
			normalized = Constants.MAX_LAT_VALUE;
		}

		return normalized;
	}

	public static double[] getDelta(Boundary bounday, int zoomLevel,
			ClusterConfiguration clusterConfiguration) {
		// Heuristic specific values and grid size dependent.
		// used in combination with zoom level.

		// xZoomLevel1 and yZoomLevel1 is used to define the size of one
		// grid-cell

		// Absolute base value of longitude distance
		int xZoomLevel1 = 480;
		// Absolute base value of latitude distance
		int yZoomLevel1 = 240;

		// Relative values, used for adjusting grid size
		double gridScaleX = clusterConfiguration.getGridx();
		double gridScaleY = clusterConfiguration.getGridy();

		double x = MathUtil.half(xZoomLevel1, zoomLevel - 1) / gridScaleX;
		double y = MathUtil.half(yZoomLevel1, zoomLevel - 1) / gridScaleY;
		return new double[] { x, y };
	}

}