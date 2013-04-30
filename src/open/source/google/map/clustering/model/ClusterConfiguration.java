package open.source.google.map.clustering.model;

import open.source.google.map.clustering.util.MathUtil.DistanceCalculationMethod;

public class ClusterConfiguration {
	private int gridx;// used
	private int gridy;// used
	private boolean doShowGridLinesInGoogleMap;
	private int outerGridExtend;
	private boolean doUpdateAllCentroidsToNearestContainingPoint;// used
	private boolean doMergeGridIfCentroidsAreCloseToEachOther;// used
	private int mergeWithin;// used
	private int minClusterSize;// used
	private int maxMarkersReturned;
	private boolean alwaysClusteringEnabledWhenZoomLevelLess;
	private int zoomlevelClusterStop;
	private String environment;
	private boolean preClustered;
	private DistanceCalculationMethod distanceCalculationMethod;

	public ClusterConfiguration(int gridx, int gridy,
			boolean doShowGridLinesInGoogleMap, int outerGridExtend,
			boolean doUpdateAllCentroidsToNearestContainingPoint,
			boolean doMergeGridIfCentroidsAreCloseToEachOther, int mergeWithin,
			int minClusterSize, int maxMarkersReturned,
			boolean alwaysClusteringEnabledWhenZoomLevelLess,
			int zoomlevelClusterStop, String environment, boolean preClustered,
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
		this.alwaysClusteringEnabledWhenZoomLevelLess = alwaysClusteringEnabledWhenZoomLevelLess;
		this.zoomlevelClusterStop = zoomlevelClusterStop;
		this.environment = environment;
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

	public boolean isAlwaysClusteringEnabledWhenZoomLevelLess() {
		return alwaysClusteringEnabledWhenZoomLevelLess;
	}

	public int getZoomlevelClusterStop() {
		return zoomlevelClusterStop;
	}

	public String getEnvironment() {
		return environment;
	}

	public boolean isPreClustered() {
		return preClustered;
	}

}
