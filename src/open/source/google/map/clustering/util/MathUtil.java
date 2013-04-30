package open.source.google.map.clustering.util;

import open.source.google.map.clustering.model.Point;

public class MathUtil {
	private static final double Exp = 2; // 2=euclid, 1=manhatten
	private static final double Pi2 = Math.PI * 2;
	private static int ROUND_CONVERT_ERROR = 5;

	// Minkowski dist
	// if lat lon precise dist is needed, use Haversine or similar formulas
	// is approx calc for clustering, no precise dist is needed
	public static double distance(Point a, Point b) {
		// lat lon wrap, values don't seem needed to be normalized to [0;1] for
		// better distance calc
		double absx = latLonDiff(a.getX(), b.getX());
		double absy = latLonDiff(a.getY(), b.getY());

		return Math.pow(Math.pow(absx, Exp) + Math.pow(Math.abs(absy), Exp),
				1.0 / Exp);
	}

	// O(1) while loop is maximum 2
	public static double latLonDiff(double from, double to) {
		double difference = to - from;
		while (difference < -LocationUtil.MAX_LENGTH_WRAP)
			difference += LocationUtil.MAX_WORD_LENGTH;
		while (difference > LocationUtil.MAX_LENGTH_WRAP)
			difference -= LocationUtil.MAX_WORD_LENGTH;
		return Math.abs(difference);

		// double differenceAngle = (to - from) % 180; //not working for -170 to
		// 170
		// return Math.Abs(differenceAngle);
	}

	public static double haversine(Point p1, Point p2) {
		return haversine(p1.getY(), p1.getX(), p2.getY(), p2.getX());
	}

	// http://en.wikipedia.org/wiki/Haversine_formula
	// Approx dist between two points on earth
	// public static double Haversine(double lat1, double lon1, double lat2,
	// double lon2)
	// {
	// final int R = 6371; // km
	// double dLat = ToRadians(lat2 - lat1);
	// double dLon = ToRadians(lon2 - lon1);
	// lat1 = ToRadians(lat1);
	// lat2 = ToRadians(lat2);

	// double a = Math.Sin(dLat / 2) * Math.Sin(dLat / 2) +
	// Math.Sin(dLon / 2) * Math.Sin(dLon / 2) * Math.Cos(lat1) *
	// Math.Cos(lat2);
	// double c = 2 * Math.Atan2(Math.Sqrt(a), Math.Sqrt(1 - a));
	// double d = R * c;
	// return d;
	// }
	// http://en.wikipedia.org/wiki/Haversine_formula
	// Approx dist between two points on earth
	// http://rosettacode.org/wiki/Haversine_formula
	public static double haversine(double lat1, double lon1, double lat2,
			double lon2) {
		final double R = 6372.8; // In kilometers
		double dLat = toRadians(lat2 - lat1);
		double dLon = toRadians(lon2 - lon1);
		lat1 = toRadians(lat1);
		lat2 = toRadians(lat2);

		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLon / 2)
				* Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
		double c = 2 * Math.asin(Math.sqrt(a));
		double d = R * c;
		return d;
	}

	public static double toRadians(double angle) {
		return Math.PI * angle / 180.0;
	}

	public static boolean isLowerThanLatMin(double d) {
		return d < LocationUtil.MIN_LAT_VALUE;
	}

	public static boolean isGreaterThanLatMax(double d) {
		return d > LocationUtil.MAX_LAT_VALUE;
	}

	// used by zoom level and deciding the grid size, O(halfSteps)
	// O(halfSteps) ~ O(maxzoom) ~ O(k) ~ O(1)
	// Google Maps doubles or halves the view for 1 step zoom level change
	public static double half(double d, int halfSteps) {
		// http://en.wikipedia.org/wiki/Decimal_degrees
		final double meter11 = 0.0001; // decimal degrees

		double half = d;
		for (int i = 0; i < halfSteps; i++) {
			half /= 2;
		}

		// TODO: double halfRounded = Math.round(half, 4);
		double halfRounded = Math.round(half);
		// avoid grid span less than level
		return halfRounded < meter11 ? meter11 : halfRounded;
	}

	// Value x which is in range [a,b] is mapped to a new value in range [c;d]
	public static double map(double x, double a, double b, double c, double d) {
		return (x - a) / (b - a) * (d - c) + c;
	}

	// Grid location are stationary, gives first left or lower grid line
	// from current latOrLon
	public static double floorLatLon(double latOrlon, double delta) {
		double floor = ((int) (latOrlon / delta)) * delta;
		if (latOrlon < 0)
			floor -= delta;

		return floor;
	}

	//
	public static boolean isLatValid(double d) {
		return LocationUtil.MIN_LAT_VALUE <= d
				&& d <= LocationUtil.MAX_LAT_VALUE;
	}

	public static boolean isLonValid(double d) {
		return LocationUtil.MIN_LON_VALUE <= d
				&& d <= LocationUtil.MAX_LON_VALUE;
	}

	// Value must be within a and b
	public static double constrain(double x, double a, double b) {
		return Math.max(a, Math.min(x, b));
	}

	// Value must be within latitude boundary
	public static double constrainLatitude(double x, double offset) {
		return Math.max(LocationUtil.MIN_LAT_VALUE + offset,
				Math.min(x, LocationUtil.MAX_LAT_VALUE - offset));
	}

	// Distance
	public static double absLat(double beg, double end) {
		double b = beg;
		double e = end;
		if (b > e) {
			e += LocationUtil.MAX_LAT_LENGTH;
		}

		double diff = e - b;
		if (diff < 0 || diff > LocationUtil.MAX_LAT_LENGTH) {
			throw new IllegalArgumentException(String.format(
					"Error AbsLat beg: {%f} end: {%f}", beg, end));
		}

		return diff;
	}

	// Distance
	public static double absLon(double beg, double end) {
		double b = beg;
		double e = end;
		if (b > e) {
			e += LocationUtil.MAX_LON_LENGTH;
		}
		double diff = e - b;
		if (diff < 0 || diff > LocationUtil.MAX_LON_LENGTH) {
			throw new IllegalArgumentException(String.format(
					"Error AbsLat beg: {%f} end: {%f}", beg, end));
		}

		return diff;
	}

	// positive version of lat, lon
	public static double positive(double latlon) {
		if (latlon < LocationUtil.MIN_LON_VALUE
				|| latlon > LocationUtil.MAX_LON_VALUE) {
			throw new IllegalArgumentException("Pos");
		}

		if (latlon < 0) {
			return latlon + LocationUtil.MAX_WORD_LENGTH;
		}

		return latlon;
	}

	// Lat or Lon
	public static double latLonToDegree(double latlon) {
		if (latlon < LocationUtil.MIN_LON_VALUE
				|| latlon > LocationUtil.MAX_LON_VALUE) {
			throw new IllegalArgumentException("LatLonToDegree");
		}

		return (latlon + LocationUtil.ANGLE_CONVERT + LocationUtil.MAX_WORD_LENGTH)
				% LocationUtil.MAX_WORD_LENGTH;
	}

	public static double degreeToLatLon(double degree) {
		if (degree < 0 || degree > 360) {
			throw new IllegalArgumentException("DegreeToLatLon");
		}

		return (degree - LocationUtil.ANGLE_CONVERT);
	}

	public static double latLonToRadian(double latlon) {
		if (latlon < LocationUtil.MIN_LON_VALUE
				|| latlon > LocationUtil.MAX_LON_VALUE) {
			throw new IllegalArgumentException("LatLonToRadian");
		}

		double degree = latLonToDegree(latlon);
		double radian = degreeToRadian(degree);
		return radian;
	}

	public static double radianNormalize(double r) {
		if (r < -Pi2 || r > Pi2) {
			throw new IllegalArgumentException("RadianNormalize");
		}

		double radian = (r + Pi2) % Pi2;
		if (radian < 0 || radian > Pi2) {
			throw new IllegalArgumentException("RadianNormalize");
		}

		return radian;
	}

	public static double degreeNormalize(double d) {
		if (d < -360 || d > 360) {
			throw new IllegalArgumentException("DegreeNormalize");
		}

		double degree = (d + 360) % 360;
		if (degree < 0 || degree > 360) {
			throw new IllegalArgumentException("DegreeNormalize");
		}

		return degree;
	}

	public static double radianToLatLon(double r) {
		double radian = radianNormalize(r);
		if (radian < 0 || radian > Pi2) {
			throw new IllegalArgumentException("RadianToLatLon");
		}

		double degree = degreeNormalize(radianToDegree(radian));
		// TODO: double degreeRounded = Math.round(degree, ROUND_CONVERT_ERROR);
		double degreeRounded = Math.round(degree);
		double latlon = degreeToLatLon(degreeRounded);
		return latlon;
	}

	public static double radianToDegree(double radian) {
		if (radian < 0 || radian > Pi2) {
			throw new IllegalArgumentException("RadianToDegree");
		}

		return (radian / Math.PI) * 180.0;
	}

	public static double degreeToRadian(double degree) {
		if (degree < 0 || degree > 360) {
			throw new IllegalArgumentException("DegreeToRadian");
		}

		return (degree * Math.PI) / 180.0;
	}

	// ]-180;180]
	// lon wrap around at value -180 and 180, nb. -180 = 180
	public static double normalizeLongitude(double lon) {
		// naive version
		// while(normalized<MinValue) ... normalized += MaxValue;

		double normalized = lon;
		if (lon < LocationUtil.MIN_LON_VALUE) {
			double m = lon % LocationUtil.MIN_LON_VALUE;
			normalized = LocationUtil.MAX_LON_VALUE + m;
		} else if (lon > LocationUtil.MAX_LON_VALUE) {
			double m = lon % LocationUtil.MAX_LON_VALUE;
			normalized = LocationUtil.MIN_LON_VALUE + m;
		}

		return normalized;
	}

	// [-90;90]
	// -90 is south pole, 90 is north pole thus -90 != 90
	// no wrap, because google map dont wrap on lat
	public static double normalizeLatitude(double lat) {
		double normalized = lat;
		if (lat < LocationUtil.MIN_LAT_VALUE) {
			// double m = lat % -LocationUtil.MAX_LAT_VALUE;
			// normalized = LocationUtil.MAX_LAT_VALUE + m;
			normalized = LocationUtil.MIN_LAT_VALUE;
		}
		if (lat > LocationUtil.MAX_LAT_VALUE) {
			// double m = lat % LocationUtil.MAX_LAT_VALUE;
			// normalized = -LocationUtil.MAX_LAT_VALUE + m;
			normalized = LocationUtil.MAX_LAT_VALUE;
		}

		return normalized;
	}

}
