package open.source.google.map.clustering.algorithm;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import open.source.google.map.clustering.model.Boundary;
import open.source.google.map.clustering.model.Point;
import open.source.google.map.clustering.util.Constants;

import org.junit.Test;

public class GridClusterAlgorithmTest {

	@Test
	public void testGetClusteredMarkers() throws Exception {
		Boundary boundary = new Boundary(Constants.MIN_LON_VALUE,
				Constants.MIN_LAT_VALUE, Constants.MAX_LON_VALUE,
				Constants.MAX_LAT_VALUE);
		List<Point> points = readPointsFromCsv();
		int zoomLevel = 5;
		int totalPoints = points.size();
		List<Point> result = new GridClusterAlgorithm().getClusteredMarkers(
				points, boundary, zoomLevel);

		int count = 0;
		for (Point r : result) {
			count += r.getCountCluster();
		}
		assertEquals(totalPoints, count);
	}

	private List<Point> readPointsFromCsv() throws Exception {
		Scanner sc = new Scanner(new File("test/Points.csv"));
		List<Point> points = new ArrayList<Point>();
		while (sc.hasNext()) {
			String p[] = sc.nextLine().split(";");
			points.add(new Point(Double.valueOf(p[0]), Double.valueOf(p[0]), 1,
					Integer.valueOf(p[2]), Integer.valueOf(p[3])));

		}
		return points;
	}

}
