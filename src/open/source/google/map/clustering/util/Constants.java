package open.source.google.map.clustering.util;

import java.text.DecimalFormat;

import open.source.google.map.clustering.model.ClusterConfiguration;
import open.source.google.map.clustering.util.MathUtil.DistanceCalculationMethod;

public class Constants {
	public static final double EPSILON = 0.0000000001;
	public static final int ROUND = 6; // lat lon, 1 meter precision
	public static final ClusterConfiguration DEFAULT_CONFIGURATION = new ClusterConfiguration(
			6, 5, true, 0, false, true, 3, 10, 500, false, 15, "dev", false,
			DistanceCalculationMethod.DEFAULT);
	public static final double MIN_LAT_VALUE = -90;
	public static final double MAX_LAT_VALUE = 90;
	public static final double MIN_LON_VALUE = -180;
	public static final double MAX_LON_VALUE = 180;
	public static final double MAX_LAT_LENGTH = 180;
	public static final double MAX_LON_LENGTH = 360;
	public static final double MAX_LENGTH_WRAP = 180;
	public static final double MAX_WORD_LENGTH = 360;
	public static final double ANGLE_CONVERT = 180;
	public static final int lAT = 0;
	public static final int LON = 1;
	public static final double EXP = 2; // 2=euclid, 1=manhatten
	public static final double PI_SQUARE = Math.PI * 2;
	public static DecimalFormat FORMATTER = new DecimalFormat("#0.####");
	public static DecimalFormat ROUND_CONVERT_ERROR = new DecimalFormat(
			"#0.#####");
}
