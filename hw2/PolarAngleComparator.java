package edu.iastate.cs228.hw2;

/**
 *  
 * @author Brad Warren: bawarren@iastate.edu
 *
 */

import java.util.Comparator;

/**
 * 
 * This class compares two points p1 and p2 by polar angle with respect to a
 * reference point.
 *
 */
public class PolarAngleComparator implements Comparator<Point> {
	private Point referencePoint;

	/**
	 * 
	 * @param p reference point
	 */
	public PolarAngleComparator(Point p) {
		referencePoint = p;
	}

	/**
	 * Use cross product and dot product to implement this method. Do not take
	 * square roots or use trigonometric functions. See the PowerPoint notes on how
	 * to carry out cross and dot products. Calls private methods crossProduct() and
	 * dotProduct().
	 * 
	 * Call comparePolarAngle() and compareDistance().
	 * 
	 * @param p1
	 * @param p2
	 * @return 0 if p1 and p2 are the same point -1 otherwise, if one of the
	 *         following three conditions holds: a) p1 and referencePoint are the
	 *         same point (hence p2 is a different point); b) neither p1 nor p2
	 *         equals referencePoint, and the polar angle of p1 with respect to
	 *         referencePoint is less than that of p2; c) neither p1 nor p2 equals
	 *         referencePoint, p1 and p2 have the same polar angle w.r.t.
	 *         referencePoint, and p1 is closer to referencePoint than p2.
	 * 
	 *         1 otherwise.
	 * 
	 */
	public int compare(Point p1, Point p2) {
		if (p1 == p2) {
			return 0;
		} else if ((p1 == referencePoint && p2 != referencePoint)
				|| (p1 != referencePoint && p2 != referencePoint && comparePolarAngle(p1, p2) == -1)
				|| (p1 != referencePoint && p2 != referencePoint && comparePolarAngle(p1, p2) == 0
						&& compareDistance(p1, p2) == -1)) {
			return -1;
		}
		return 1;
	}

	/**
	 * Compare the polar angles of two points p1 and p2 with respect to
	 * referencePoint. Use cross products. Do not use trigonometric functions.
	 * 
	 * All polar angles are within the range [0, 2 * pi).
	 * 
	 * Ought to be private but made public for testing purpose.
	 * 
	 * @param p1
	 * @param p2
	 * @return 0 if one of the following two situations below happens: a) p1 and p2
	 *         are equal to referencePoint b) none is equal to referencePoint and
	 *         both have the same polar angle w.r.t. it. -1 otherwise, if p1 equals
	 *         referencePoint; otherwise, if p2 is not equal to referencePoint and
	 *         one of the five situations below happens: a) p1.y = p2.y =
	 *         referencePoint.y and p1.x > referencePoint.x and p2.x <
	 *         referencePoint.x. b) p1.y > referencePoint.y and p2.y <=
	 *         referencePoint.y; c) p1.y = referencePoint.y and p2.y <
	 *         referencePoint.y; d) p1.y > referencePoint.y and p2.y >
	 *         referencePoint.y and the cross product of (p1 - referencePoint) and
	 *         (p2 - referencePoint) is greater than 0; e) p1.y < referencePoint.y
	 *         and p2.y < referencePoint.y and the cross product of (p1 -
	 *         referencePoint) and (p2 - referencePoint) is greater than 0. 1
	 *         otherwise.
	 */
	public int comparePolarAngle(Point p1, Point p2) {
		if ((p1 == referencePoint && p2 == referencePoint) // case of 0 part a
				|| (p1 != referencePoint && p2 != referencePoint && p1 == p2)) { // case of 0 parts b
			return 0;
		} else if (p1 == referencePoint) { // case of -1
			return -1;
		}else if(p2 != referencePoint && p1.getY() == p2.getY() && p2.getY() == referencePoint.getY() // case of -1 part a
				&& p1.getX() > referencePoint.getX() && p2.getX() < referencePoint.getX()) {
			return -1;
		}else if(p2 != referencePoint && p1.getY() > referencePoint.getY() && p2.getY() <= referencePoint.getY()) { // case of -1 part b
			return -1;
		}else if(p2 != referencePoint && p1.getY() == referencePoint.getY() && p2.getY() < referencePoint.getY()) { // case of -1 part c
			return -1;
		}else if(p2 != referencePoint && p1.getY() > referencePoint.getY() && p2.getY() > referencePoint.getY() && crossProduct(p1, p2) > 0) { // case of -1 part d
			return -1;
		}else if(p2 != referencePoint && p1.getY() < referencePoint.getY() && p2.getY() < referencePoint.getY() // case of -1 part e
				&& crossProduct(p1, p2) > 0) {
			return -1;
		}else {
			return 1;
		}
	}

	/**
	 * Compare the distances of two points p1 and p2 to referencePoint. Use dot
	 * products. Do not take square roots.
	 * 
	 * Ought to be private but made public for testing purpose.
	 * 
	 * @param p1
	 * @param p2
	 * @return 0 if p1 and p2 are equidistant to referencePoint -1 if p1 is closer
	 *         to referencePoint than p2 1 otherwise
	 */
	public int compareDistance(Point p1, Point p2) {
		if (dotProduct(p1, referencePoint) == dotProduct(p2, referencePoint)) {
			return 0;
		} else if (dotProduct(p1, referencePoint) < dotProduct(p2, referencePoint)) {
			return -1;
		} else {
			return 1;
		}
	}

	/**
	 * 
	 * @param p1
	 * @param p2
	 * @return cross product of two vectors p1 - referencePoint and p2 -
	 *         referencePoint
	 */
	private int crossProduct(Point p1, Point p2) {
		int p1x = p1.getX() - referencePoint.getX();
		int p1y = p1.getY() - referencePoint.getY();
		int p2x = p2.getX() - referencePoint.getX();
		int p2y = p2.getY() - referencePoint.getY();
		return (p1x * p2y) - (p1y * p2x);
	}

	/**
	 * 
	 * @param p1
	 * @param p2
	 * @return dot product of two vectors p1 - referencePoint and p2 -
	 *         referencePoint
	 */
	private int dotProduct(Point p1, Point p2) {
		int p1x = p1.getX() - referencePoint.getX();
		int p1y = p1.getY() - referencePoint.getY();
		int p2x = p2.getX() - referencePoint.getX();
		int p2y = p2.getY() - referencePoint.getY();
		return (p1x * p2x) + (p1y * p2y);
	}
}
