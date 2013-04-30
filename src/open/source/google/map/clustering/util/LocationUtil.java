package open.source.google.map.clustering.util;

import open.source.google.map.clustering.model.Boundary;
import open.source.google.map.clustering.model.ClusterConfiguration;

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