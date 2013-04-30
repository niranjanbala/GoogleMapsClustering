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