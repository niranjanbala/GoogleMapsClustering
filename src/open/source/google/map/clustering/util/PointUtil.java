package open.source.google.map.clustering.util;

import static open.source.google.map.clustering.util.MathUtil.latLonToRadian;
import static open.source.google.map.clustering.util.MathUtil.radianToLatLon;

import java.util.List;

import open.source.google.map.clustering.model.Boundary;
import open.source.google.map.clustering.model.Point;
import open.source.google.map.clustering.util.MathUtil.DistanceCalculationMethod;

public class PointUtil {

	public static Point getNearestPoint(Point from, List<Point> points,
			DistanceCalculationMethod method) {
		double min = Double.MAX_VALUE;
		Point nearest = null;
		for (Point p : points) {
			double d = MathUtil.distance(from, p, method);
			if (d >= min) {
				continue;
			}
			// update
			min = d;
			nearest = p;
		}
		return nearest;
	}

	public static Point getCentroidFromPoints(List<Point> points) {
		int count = points.size();
		if (points == null || points.isEmpty())
			return null;
		if (count == 1) {
			return points.get(0);
		}
		// http://en.wikipedia.org/wiki/Circular_mean
		// http://stackoverflow.com/questions/491738/how-do-you-calculate-the-average-of-a-set-of-angles
		/*
		 * 1/N* sum_i_from_1_to_N sin(a[i]) a = atan2
		 * --------------------------- 1/N* sum_i_from_1_to_N cos(a[i])
		 */
		double lonSin = 0;
		double lonCos = 0;
		double latSin = 0;
		double latCos = 0;
		for (Point p : points) {
			lonSin += Math.sin(latLonToRadian(p.getX()));
			lonCos += Math.cos(latLonToRadian(p.getX()));
			latSin += Math.sin(latLonToRadian(p.getY()));
			latCos += Math.cos(latLonToRadian(p.getY()));
		}
		lonSin /= count;
		lonCos /= count;
		double radx = 0;
		double rady = 0;
		if (Math.abs(lonSin - 0) > Constants.EPSILON
				&& Math.abs(lonCos - 0) > Constants.EPSILON) {
			radx = Math.atan2(lonSin, lonCos);
			rady = Math.atan2(latSin, latCos);
		}
		double x = radianToLatLon(radx);
		double y = radianToLatLon(rady);
		Point point = new Point(x, y, 0, Constants.MARKER_CLUSTER_TYPE);
		point.setCountCluster(count);
		return point;
	}

	public static int[] getPointMappedIds(Point p, Boundary grid,
			double deltax, double deltay) {
		// TODO: double relativeX = p.getX() - grid.getMinx();
		double relativeY = p.getY() - grid.getMiny();
		double idx, idy;

		// Naive version, lon points near 180 and lat points near 90 are not
		// clustered together
		// idx = (int)(relativeX / deltax);
		// idy = (int)(relativeY / deltay);
		// end Naive version

		/*
		 * You have to draw a line with longitude values 180, -180 on papir to
		 * understand this
		 * 
		 * e.g. _deltaX = 20 longitude 150 170 180 -170 -150 | | | |
		 * 
		 * 
		 * idx = 7 8 9 -9 -8 -10
		 * 
		 * here we want idx 8, 9, -10 and -9 be equal to each other, we set them
		 * to idx=8 then the longitudes from 170 to -170 will be clustered
		 * together
		 */
		double overlapMapMinX = (int) (Constants.MIN_LON_VALUE / deltax) - 1;
		double overlapMapMaxX = (int) (Constants.MAX_LON_VALUE / deltax);

		// The deltaX = 20 example scenario, then set the value 9 to 8 and -10
		// to -9

		// Similar to if (LatLonInfo.MaxLonValue % deltax == 0) without floating
		// presicion issue
		if (Math.abs(Constants.MAX_LON_VALUE % deltax - 0) < Constants.EPSILON) {
			overlapMapMaxX--;
			overlapMapMinX++;
		}

		double idxx = (int) (p.getX() / deltax);
		if (p.getX() < 0)
			idxx--;

		if (Math.abs(Constants.MAX_LON_VALUE % p.getX() - 0) < Constants.EPSILON) {
			if (p.getX() < 0)
				idxx++;
			else
				idxx--;
		}

		if (idxx == overlapMapMinX)
			idxx = overlapMapMaxX;

		idx = idxx;

		// Latitude never wraps around with Google Maps, ignore 90, -90
		// wrap-around for latitude
		idy = (int) (relativeY / deltay);

		return new int[] { (int) idx, (int) idy };
	}

	public static String getKey(int x, int y) {
		return x + "-" + y;
	}
}
