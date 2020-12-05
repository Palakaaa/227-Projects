package edu.iastate.cs228.hw2;

/**
 *  
 * @author Brad Warren: bawarren@iastate.edu 
 *
 */

import java.io.FileNotFoundException;
import java.lang.NumberFormatException;
import java.lang.IllegalArgumentException;
import java.util.Arrays;
import java.util.InputMismatchException;

/**
 * 
 * This class implements the mergesort algorithm.
 *
 */

public class MergeSorter extends AbstractSorter {
	// Other private instance variables if you need ...

	/**
	 * Constructor takes an array of points. It invokes the superclass constructor,
	 * and also set the instance variables algorithm in the superclass.
	 * 
	 * @param pts input array of integers
	 */
	public MergeSorter(Point[] pts) {
		super(pts);
		algorithm = "mergesort";
	}

	/**
	 * Perform mergesort on the array points[] of the parent class AbstractSorter.
	 * 
	 */
	@Override
	public void sort() {
		mergeSortRec(points);
	}

	/**
	 * This is a recursive method that carries out mergesort on an array pts[] of
	 * points. One way is to make copies of the two halves of pts[], recursively
	 * call mergeSort on them, and merge the two sorted subarrays into pts[].
	 * 
	 * @param pts point array
	 */
	private void mergeSortRec(Point[] pts) {
		if (pts.length <= 1) {
			return;
		}

		int firstLength = pts.length / 2;
		Point[] first = Arrays.copyOf(pts, firstLength);
		Point[] second = Arrays.copyOfRange(pts, firstLength, pts.length);

		mergeSortRec(first); // sorts first half of pts
		mergeSortRec(second); // sorts second half of pts
		Point[] result = merge(first, second); // merges the two arrays

		for (int i = 0; i < result.length; ++i) { // sets the result of the two merged arrays into pts
			pts[i] = result[i];
		}

	}

	private Point[] merge(Point[] first, Point[] second) {

		Point[] result = new Point[first.length + second.length]; // resulting array of points

		int i = 0; // starting index in a
		int j = 0; // starting index in b
		final int iMax = first.length; // max index in a
		final int jMax = second.length; // max index in b
		int k = 0; // index in result

		while (i < iMax && j < jMax) {
			if (pointComparator.compare(first[i], second[j]) == 0 // checks if they are equal
					|| pointComparator.compare(first[i], second[j]) < 0) { // checks if first is less than second
				result[k] = first[i];
				i = i + 1;
				k = k + 1;
			} else {
				result[k] = second[j];
				j = j + 1;
				k = k + 1;
			}
		}
		while (i < iMax) {
			result[k] = first[i];
			i = i + 1;
			k = k + 1;
		}
		while (j < jMax) {
			result[k] = second[j];
			j = j + 1;
			k = k + 1;
		}

		return result;
	}

}
