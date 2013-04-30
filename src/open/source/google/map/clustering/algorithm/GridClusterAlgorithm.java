package open.source.google.map.clustering.algorithm;

import static open.source.google.map.clustering.util.MathUtil.latLonToRadian;
import static open.source.google.map.clustering.util.MathUtil.radianToLatLon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import open.source.google.map.clustering.model.Boundary;
import open.source.google.map.clustering.model.Bucket;
import open.source.google.map.clustering.model.ClusterConfiguration;
import open.source.google.map.clustering.model.Point;
import open.source.google.map.clustering.util.Constants;
import open.source.google.map.clustering.util.LocationUtil;
import open.source.google.map.clustering.util.MathUtil;

public class GridClusterAlgorithm {
	private ClusterConfiguration clusterConfiguration;
	private double delta[];

	public GridClusterAlgorithm(ClusterConfiguration clusterConfiguration) {
		this.clusterConfiguration = clusterConfiguration;
	}

	public GridClusterAlgorithm() {
		this.clusterConfiguration = Constants.DEFAULT_CONFIGURATION;
	}

	public List<Point> getClusteredMarkers(List<Point> points,
			Boundary boundary, int zoomLevel) {
		delta = LocationUtil
				.getDelta(boundary, zoomLevel, clusterConfiguration);
		Map<String, Bucket> bucketMap = prepareBucketMap(points, boundary,
				zoomLevel);
		setCentroidForAllBuckets(bucketMap);
		// Merge if gridpoint is to close
		if (clusterConfiguration.isDoMergeGridIfCentroidsAreCloseToEachOther()) {
			mergeClustersGrid(bucketMap);
		}
		if (clusterConfiguration
				.isDoUpdateAllCentroidsToNearestContainingPoint()) {
			updateAllCentroidsToNearestContainingPoint(bucketMap);
		}
		// // Check again
		// // Merge if gridpoint is to close
		if (clusterConfiguration.isDoMergeGridIfCentroidsAreCloseToEachOther()
				&& clusterConfiguration
						.isDoUpdateAllCentroidsToNearestContainingPoint()) {
			mergeClustersGrid(bucketMap);
			// And again set centroid to closest point in bucket
			updateAllCentroidsToNearestContainingPoint(bucketMap);
		}
		return getClusterResult(bucketMap);
	}

	private void updateAllCentroidsToNearestContainingPoint(
			Map<String, Bucket> bucketMap) {
		for (Bucket bucket : bucketMap.values()) {
			updateCentroidToNearestContainingPoint(bucket);
		}

	}

	// // Update centroid location to nearest point,
	// // e.g. if you want to show cluster point on a real existing point area
	// // O(n)
	private void updateCentroidToNearestContainingPoint(Bucket bucket) {
		if (bucket == null || bucket.getCentroid() == null
				|| bucket.getPoints() == null || bucket.getPoints().size() == 0) {
			return;
		}

		Point closest = getNearestPoint(bucket.getCentroid(),
				bucket.getPoints());
		bucket.setCentroid(closest);
	}

	private Point getNearestPoint(Point from, List<Point> points) {
		double min = Double.MAX_VALUE;
		Point nearest = null;
		for (Point p : points) {
			double d = MathUtil.distance(from, p);
			if (d >= min) {
				continue;
			}
			// update
			min = d;
			nearest = p;
		}
		return nearest;
	}

	private void mergeClustersGrid(Map<String, Bucket> bucketMap) {
		for (String key : bucketMap.keySet()) {
			Bucket bucket = bucketMap.get(key);
			if (!bucket.isUsed()) {
				continue;
			}
			int x = bucket.getX();
			int y = bucket.getY();

			// get keys for neighbors
			String N = getKey(x, y + 1);
			String NE = getKey(x + 1, y + 1);
			String E = getKey(x + 1, y);
			String SE = getKey(x + 1, y - 1);
			String S = getKey(x, y - 1);
			String SW = getKey(x - 1, y - 1);
			String W = getKey(x - 1, y);
			String NW = getKey(x - 1, y - 1);
			String neighbors[] = { N, NE, E, SE, S, SW, W, NW };
			mergeClustersGridHelper(key, neighbors, bucketMap);
		}

	}

	private void mergeClustersGridHelper(String currentKey, String[] neighbors,
			Map<String, Bucket> bucketMap) {
		double minDistX = delta[0] / clusterConfiguration.getMergeWithin();
		double minDistY = delta[1] / clusterConfiguration.getMergeWithin();
		// If clusters in grid are too close to each other, merge them
		double withinDist = Math.max(minDistX, minDistY);

		for (String neighborKey : neighbors) {
			if (!bucketMap.containsKey(neighborKey))
				continue;

			Bucket neighbor = bucketMap.get(neighborKey);
			if (!neighbor.isUsed())
				continue;
			Bucket current = bucketMap.get(currentKey);
			double dist = MathUtil.distance(current.getCentroid(),
					neighbor.getCentroid());
			if (dist > withinDist)
				continue;
			current.getPoints().addAll(neighbor.getPoints());// O(n)
			// recalc centroid
			Point cp = getCentroidFromPoints(current.getPoints());
			current.setCentroid(cp);
			neighbor.setUsed(false); // merged, then not used anymore
			neighbor.getPoints().clear(); // clear mem
		}

	}

	private String getKey(int x, int y) {
		return x + "-" + y;
	}

	private Map<String, Bucket> prepareBucketMap(List<Point> points,
			Boundary boundary, int zoomLevel) {
		boolean filterData = LocationUtil.canFilterData(zoomLevel);
		Map<String, Bucket> bucketMap = new HashMap<String, Bucket>();

		double deltax = delta[0];
		double deltay = delta[1];
		for (Point p : points) {
			int pointMappedId[] = null;
			if (filterData && withinBoundary(p, boundary)) {
				pointMappedId = getPointMappedIds(p, boundary, deltax, deltay);
			} else {
				pointMappedId = getPointMappedIds(p, boundary, deltax, deltay);
			}
			if (pointMappedId != null) {
				String key = getKey(pointMappedId[0], pointMappedId[1]);
				if (bucketMap.containsKey(key)) {
					bucketMap.get(key).getPoints().add(p);
				} else {
					Bucket b = new Bucket(pointMappedId[0], pointMappedId[1],
							key);
					b.getPoints().add(p);
					bucketMap.put(key, b);
				}
			}
		}
		return bucketMap;
	}

	private List<Point> getClusterResult(Map<String, Bucket> bucketMap) {
		List<Point> clusterPoints = new ArrayList<Point>();
		for (Bucket bucket : bucketMap.values()) {
			if (!bucket.isUsed())
				continue;
			if (bucket.getPoints().size() < clusterConfiguration
					.getMinClusterSize())
				clusterPoints.addAll(bucket.getPoints());
			else {
				bucket.getCentroid().setCountCluster(bucket.getPoints().size());
				clusterPoints.add(bucket.getCentroid());
			}
		}
		return clusterPoints;
	}

	private void setCentroidForAllBuckets(Map<String, Bucket> bucketMap) {
		for (Bucket bucket : bucketMap.values()) {
			bucket.setCentroid(getCentroidFromPoints(bucket.getPoints()));
		}
	}

	private Point getCentroidFromPoints(List<Point> points) {
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
		Point point = new Point();
		point.setX(x);
		point.setY(y);
		point.setCountCluster(count);
		return point;
	}

	private int[] getPointMappedIds(Point p, Boundary grid, double deltax,
			double deltay) {
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

	private boolean withinBoundary(Point p, Boundary boundary) {
		return LocationUtil.IsInside(boundary, p);
	}
}
