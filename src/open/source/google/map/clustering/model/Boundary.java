package open.source.google.map.clustering.model;

import open.source.google.map.clustering.util.MathUtil;

public class Boundary extends Rectangle {
	public Boundary() {
		super(0, 0, 0, 0);
	}

	public Boundary(Boundary b) {
		super(b.minx, b.miny, b.maxx, b.maxy);
	}

	// / <summary>
	// / Normalize lat and lon values to their boundary values
	// / </summary>
	public void normalize() {
		this.miny = MathUtil.normalizeLatitude(miny);
		this.maxy = MathUtil.normalizeLatitude(maxy);
		this.minx = MathUtil.normalizeLongitude(minx);
		this.maxx = MathUtil.normalizeLongitude(maxx);
	}

	public void validateLatLon() {
		if (!(MathUtil.isLatValid(miny) && MathUtil.isLatValid(maxy))
				&& MathUtil.isLonValid(minx) && MathUtil.isLonValid(maxx)) {
			throw new IllegalArgumentException(
					"input Boundary.ValidateLatLon() error " + this);
		}
	}

	// Distance lon
	public double getAbsoluteX() {
		return MathUtil.absLon(minx, maxx);
	}

	// Distance lat
	public double getAbsoluteY() {
		return MathUtil.absLat(miny, maxy);
	}
}
