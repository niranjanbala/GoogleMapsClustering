package open.source.google.map.clustering.model;

import open.source.google.map.clustering.util.MathUtil.DistanceCalculationMethod;

public class ClusterConfiguration {
	private int gridx;
	private int gridy;
	private boolean doUpdateAllCentroidsToNearestContainingPoint;
	private boolean doMergeGridIfCentroidsAreCloseToEachOther;
	private int mergeWithin;
	private int minClusterSize;
	private int zoomlevelClusterStop;
	private DistanceCalculationMethod distanceCalculationMethod;
	private boolean alwaysClusteringEnabled;
	private boolean doShowGridLinesInGoogleMap;// UNUSED
	private int outerGridExtend;// UNUSED
	private int maxMarkersReturned;// UNUSED
	private boolean preClustered;// UNUSED

	public ClusterConfiguration(int gridx, int gridy,
			boolean doShowGridLinesInGoogleMap, int outerGridExtend,
			boolean doUpdateAllCentroidsToNearestContainingPoint,
			boolean doMergeGridIfCentroidsAreCloseToEachOther, int mergeWithin,
			int minClusterSize, int maxMarkersReturned,
			boolean alwaysClusteringEnabled, int zoomlevelClusterStop,
			boolean preClustered,
			DistanceCalculationMethod distanceCalculationMethod) {
		super();
		this.gridx = gridx;
		this.gridy = gridy;
		this.doShowGridLinesInGoogleMap = doShowGridLinesInGoogleMap;
		this.outerGridExtend = outerGridExtend;
		this.doUpdateAllCentroidsToNearestContainingPoint = doUpdateAllCentroidsToNearestContainingPoint;
		this.doMergeGridIfCentroidsAreCloseToEachOther = doMergeGridIfCentroidsAreCloseToEachOther;
		this.mergeWithin = mergeWithin;
		this.minClusterSize = minClusterSize;
		this.maxMarkersReturned = maxMarkersReturned;
		this.alwaysClusteringEnabled = alwaysClusteringEnabled;
		this.zoomlevelClusterStop = zoomlevelClusterStop;
		this.preClustered = preClustered;
		this.distanceCalculationMethod = distanceCalculationMethod;
	}

	public DistanceCalculationMethod getDistanceCalculationMethod() {
		return distanceCalculationMethod;
	}

	public int getGridx() {
		return gridx;
	}

	public int getGridy() {
		return gridy;
	}

	public boolean isDoShowGridLinesInGoogleMap() {
		return doShowGridLinesInGoogleMap;
	}

	public int getOuterGridExtend() {
		return outerGridExtend;
	}

	public boolean isDoUpdateAllCentroidsToNearestContainingPoint() {
		return doUpdateAllCentroidsToNearestContainingPoint;
	}

	public boolean isDoMergeGridIfCentroidsAreCloseToEachOther() {
		return doMergeGridIfCentroidsAreCloseToEachOther;
	}

	public int getMergeWithin() {
		return mergeWithin;
	}

	public int getMinClusterSize() {
		return minClusterSize;
	}

	public int getMaxMarkersReturned() {
		return maxMarkersReturned;
	}

	public boolean enableClusteringAlways() {
		return alwaysClusteringEnabled;
	}

	public int getZoomlevelClusterStop() {
		return zoomlevelClusterStop;
	}

	public boolean isPreClustered() {
		return preClustered;
	}

}
