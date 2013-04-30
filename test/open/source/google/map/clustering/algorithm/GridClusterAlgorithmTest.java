package open.source.google.map.clustering.algorithm;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import open.source.google.map.clustering.model.Boundary;
import open.source.google.map.clustering.model.ClusterPoint;
import open.source.google.map.clustering.util.Constants;

import org.junit.Test;

public class GridClusterAlgorithmTest {

	@Test
	public void testGetClusteredMarkers() throws Exception {
		Boundary boundary = new Boundary(Constants.MIN_LON_VALUE,
				Constants.MIN_LAT_VALUE, Constants.MAX_LON_VALUE,
				Constants.MAX_LAT_VALUE);
		List<ClusterPoint> points = readPointsFromCsv();
		int zoomLevel = 5;
		int totalPoints = points.size();
		List<ClusterPoint> result = new GridClusterAlgorithm()
				.getClusteredMarkers(points, boundary, zoomLevel);

		int count = 0;
		for (ClusterPoint r : result) {
			count += r.getCountCluster();
		}
		assertEquals(totalPoints, count);
	}

	private List<ClusterPoint> readPointsFromCsv() throws Exception {
		Scanner sc = new Scanner(new File("test/Points.csv"));
		List<ClusterPoint> points = new ArrayList<ClusterPoint>();
		while (sc.hasNext()) {
			String p[] = sc.nextLine().split(";");
			points.add(new Point(Double.valueOf(p[0]), Double.valueOf(p[0]),
					Integer.valueOf(p[2]), Integer.valueOf(p[3])));

		}
		return points;
	}

}
