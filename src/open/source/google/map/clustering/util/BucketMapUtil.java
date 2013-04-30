package open.source.google.map.clustering.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import open.source.google.map.clustering.model.Boundary;
import open.source.google.map.clustering.model.Bucket;
import open.source.google.map.clustering.model.ClusterConfiguration;
import open.source.google.map.clustering.model.Point;
import open.source.google.map.clustering.util.MathUtil.DistanceCalculationMethod;

public class BucketMapUtil {
	private double delta[];
	private double mergeWithin;
	private DistanceCalculationMethod distanceCalculationMethod;
	private ClusterConfiguration clusterConfiguration;

	public BucketMapUtil(ClusterConfiguration clusterConfiguration) {
		super();
		this.clusterConfiguration = clusterConfiguration;
		this.mergeWithin = clusterConfiguration.getMergeWithin();
		this.distanceCalculationMethod = clusterConfiguration
				.getDistanceCalculationMethod();
	}

	public List<Point> getClusterResult(List<Point> points, Boundary boundary,
			int zoomLevel) {
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

	private void mergeClustersGrid(Map<String, Bucket> bucketMap) {
		for (String key : bucketMap.keySet()) {
			Bucket bucket = bucketMap.get(key);
			if (!bucket.isUsed()) {
				continue;
			}
			int x = bucket.getX();
			int y = bucket.getY();

			// get keys for neighbors
			String N = PointUtil.getKey(x, y + 1);
			String NE = PointUtil.getKey(x + 1, y + 1);
			String E = PointUtil.getKey(x + 1, y);
			String SE = PointUtil.getKey(x + 1, y - 1);
			String S = PointUtil.getKey(x, y - 1);
			String SW = PointUtil.getKey(x - 1, y - 1);
			String W = PointUtil.getKey(x - 1, y);
			String NW = PointUtil.getKey(x - 1, y - 1);
			String neighbors[] = { N, NE, E, SE, S, SW, W, NW };
			mergeClustersGridHelper(key, neighbors, bucketMap);
		}

	}

	private void mergeClustersGridHelper(String currentKey, String[] neighbors,
			Map<String, Bucket> bucketMap) {
		double minDistX = delta[0] / mergeWithin;
		double minDistY = delta[1] / mergeWithin;
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
					neighbor.getCentroid(), distanceCalculationMethod);
			if (dist > withinDist)
				continue;
			current.getPoints().addAll(neighbor.getPoints());// O(n)
			// recalc centroid
			Point cp = PointUtil.getCentroidFromPoints(current.getPoints());
			current.setCentroid(cp);
			neighbor.setUsed(false); // merged, then not used anymore
			neighbor.getPoints().clear(); // clear mem
		}
	}

	private void updateAllCentroidsToNearestContainingPoint(
			Map<String, Bucket> bucketMap) {
		for (Bucket bucket : bucketMap.values()) {
			updateCentroidToNearestContainingPoint(bucket);
		}
	}

	private void setCentroidForAllBuckets(Map<String, Bucket> bucketMap) {
		for (Bucket bucket : bucketMap.values()) {
			bucket.setCentroid(PointUtil.getCentroidFromPoints(bucket
					.getPoints()));
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
		Point closest = PointUtil.getNearestPoint(bucket.getCentroid(),
				bucket.getPoints(), distanceCalculationMethod);
		bucket.setCentroid(closest);
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

	private Map<String, Bucket> prepareBucketMap(List<Point> points,
			Boundary boundary, int zoomLevel) {
		boolean filterData = LocationUtil.canFilterData(zoomLevel);
		Map<String, Bucket> bucketMap = new HashMap<String, Bucket>();

		double deltax = delta[0];
		double deltay = delta[1];
		for (Point p : points) {
			int pointMappedId[] = null;
			if (filterData && p.isInside(boundary)) {
				pointMappedId = PointUtil.getPointMappedIds(p, boundary,
						deltax, deltay);
			} else {
				pointMappedId = PointUtil.getPointMappedIds(p, boundary,
						deltax, deltay);
			}
			if (pointMappedId != null) {
				String key = PointUtil.getKey(pointMappedId[0],
						pointMappedId[1]);
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
}
