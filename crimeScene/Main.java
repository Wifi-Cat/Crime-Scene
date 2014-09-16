package crimeScene;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		// read the first line
		int numberOfCrimeScenes = sc.nextInt();
		for (int n = 0; n < numberOfCrimeScenes; n++) {
			// read the number of pieces of evidence
			int numberOfEvidences = sc.nextInt();
			Point[] points = new Point[numberOfEvidences];
			for (int m = 0; m < numberOfEvidences; m++) {
				// read all the points
				int x = sc.nextInt();
				int y = sc.nextInt();
				points[m] = new Point(x, y);
			}
			// process the points
			System.out.printf("CRIME SCENE %d: ", n + 1);
			List<Point> hull;
			hull = findConvexHull(points);
			printHullLength(hull);
		}
	}

	/**
	 * Compute hull length and print it. Pre: hull.size() > 0
	 * 
	 * @param hull
	 */
	private static void printHullLength(List<Point> hull) {
		double length = 0;
		Iterator<Point> it = hull.iterator();
		Point first = it.next(), second;
		while (it.hasNext()) {
			second = it.next();
			length += Math.sqrt(distSquared(first, second));
			first = second;
		}
		length += Math.sqrt(distSquared(hull.get(0), first));
		System.out.printf("%.3f feet\n", length);
	}

	private static double distSquared(Point p1, Point p2) {
		return Math.pow((p1.x - p2.x), 2) + Math.pow((p1.y - p2.y), 2);
	}

	private static int orientation(Point p, Point q, Point r) {
		double det = (q.x - p.x) * (r.y - p.y) - (q.y - p.y) * (r.x - p.x);
		if (det > 0) {
			return 1; // counterclockwise
		} else if (det < 0) {
			return -1; // clockwise
		} else {
			return 0; // colinear
		}
	}

	private static List<Point> findConvexHull(Point[] points) {
		LinkedList<Point> hull = new LinkedList<Point>();
		Point min = findSmallestYX(points);
		Point p, q = min;
		int i = 0;
		do {
			hull.addLast(q);
			p = hull.get(i);
			q = nextHullPoint(points, p);
			i++;
		} while (!q.equals(min));
		// close hull
		hull.add(hull.getFirst());
		return hull;
	}

	private static Point findSmallestYX(Point[] points) {
		Point candidate = points[0];
		for (Point point : points) {
			if (point.x <= candidate.x) {
				if (point.y < candidate.y) {
					candidate = point;
				}
			}
		}
		return candidate;
	}

	private static Point nextHullPoint(Point[] points, Point p) {
		Point q = p;
		for (final Point r : points) {
			int o = orientation(p, q, r);
			if (o == 1 || o == 0 && distSquared(p, r) > distSquared(p, q)) {
				q = r;
			}
		}
		return q;
	}
}
