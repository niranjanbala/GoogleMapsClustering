package open.source.google.map.clustering.algorithm;

import java.util.List;

import open.source.google.map.clustering.model.Boundary;
import open.source.google.map.clustering.model.ClusterConfiguration;
import open.source.google.map.clustering.model.Point;
import open.source.google.map.clustering.util.BucketMapUtil;
import open.source.google.map.clustering.util.Constants;

public class GridClusterAlgorithm {
	private ClusterConfiguration clusterConfiguration;
	private BucketMapUtil bucketMapUtil;

	public GridClusterAlgorithm(ClusterConfiguration clusterConfiguration) {
		this.clusterConfiguration = clusterConfiguration;
		this.bucketMapUtil = new BucketMapUtil(clusterConfiguration);
	}

	public GridClusterAlgorithm() {
		this(Constants.DEFAULT_CONFIGURATION);
	}

	public List<Point> getClusteredMarkers(List<Point> points,
			Boundary boundary, int zoomLevel) {
		if (!clusterConfiguration.enableClusteringAlways()
				&& zoomLevel >= clusterConfiguration.getZoomlevelClusterStop()) {
			return points;
		}
		return this.bucketMapUtil.getClusterResult(points, boundary, zoomLevel);
	}

}
