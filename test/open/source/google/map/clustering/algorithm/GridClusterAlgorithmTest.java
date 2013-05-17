package open.source.google.map.clustering.algorithm;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import open.source.google.map.clustering.model.Boundary;
import open.source.google.map.clustering.model.ClusterPoint;
import open.source.google.map.clustering.util.Constants;

import org.junit.Test;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;

public class GridClusterAlgorithmTest {

	@Test
	public void testGetClusteredMarkers() throws Exception {
		Boundary boundary = new Boundary(Constants.MIN_LON_VALUE,
				Constants.MAX_LON_VALUE, Constants.MIN_LAT_VALUE,
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
		System.out.println(result.size());
		assertEquals(totalPoints, count);
	}

	@Test
	public void testFoo() throws Exception {
		JsonArray array = fetchData();
		/*
		 * lat1:33.71033395243858 lat2:33.43801824583935
		 * lon1:-111.53628503796386 lon2:-111.53628503796386
		 */
		Double lat1 = 33.71033395243858;
		Double lat2 = 33.43801824583935;
		Double lon1 = -111.53628503796386;
		Double lon2 = -111.83628503796386;
		int zoomLevel = 11;
		test(array, lat2, lat1, lon2, lon1, zoomLevel);
	}

	@Test
	public void testFoo2() throws Exception {
		JsonArray array = fetchData();
		/*
		 * lat1:33.71033395243858 lat2:33.43801824583935
		 * lon1:-111.53628503796386 lon2:-111.53628503796386
		 */
		Double lat1 = 34.95756070569858;
		Double lat2 = 32.78682872515491;
		Double lon1 = -107.78239404675293;
		Double lon2 = -115.27506982800293;
		int zoomLevel = 11;
		test(array, lat2, lat1, lon2, lon1, zoomLevel);
	}

	private void test(JsonArray array, Double lat1, Double lat2, Double lon1,
			Double lot2, int zoomLevel) {
		List<ClusterPoint> points = new ArrayList<ClusterPoint>();
		for (int i = 0; i < array.size(); i++) {
			points.add(new BusinessPoints(array.get(i).asObject()));
		}
		Boundary boundary = new Boundary(lon1, lot2, lat1, lat2);
		int count = 0;
		int total = 0;
		for (ClusterPoint p : points) {
			total++;
			if (p.isInside(boundary)) {
				count++;
			}
		}
		System.out.println(count);
		System.out.println(total);
		List<ClusterPoint> result = new GridClusterAlgorithm()
				.getClusteredMarkers(points, boundary, zoomLevel);
		int ctotal = 0;
		for (ClusterPoint r : result) {
			ctotal += r.getCountCluster();
		}
		System.out.println(result.size());
		System.out.println(ctotal);
		assertEquals(count, ctotal);
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

	public static JsonArray fetchData() throws FileNotFoundException {
		Scanner sc = new Scanner(new File(
				"data/yelp/yelp_training_set_business.json"));
		StringBuilder sb = new StringBuilder();
		while (sc.hasNext()) {
			sb.append(sc.nextLine());
		}
		JsonArray array = JsonArray.readFrom(sb.toString());
		return array;
	}

	public class BusinessPoints extends ClusterPoint {
		private JsonObject object;

		public BusinessPoints(JsonObject object) {
			super();
			this.object = object;
		}

		@Override
		public double getX() {
			return object.get("longitude").asDouble();
		}

		@Override
		public double getY() {
			return object.get("latitude").asDouble();
		}

		@Override
		public void setX(double arg0) {
			object.add("longitude", Double.valueOf(arg0));
		}

		@Override
		public void setY(double arg0) {
			object.add("latitude", arg0);
		}

		public String getName() {
			return object.get("name").asString();
		}

		public String getAddress() {
			return object.get("full_address").asString();
		}

		public String getMarkerLabel() {
			if (this.isClusterPoint()) {
				return String.valueOf(this.getCountCluster());
			}
			return this.getName();
		}
	}

}
