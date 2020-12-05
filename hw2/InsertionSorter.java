package edu.iastate.cs228.hw2;

/**
 *  
 * @author Brad Warren: bawarren@iastate.edu
 *
 */

import java.io.FileNotFoundException;
import java.lang.NumberFormatException;
import java.lang.IllegalArgumentException;
import java.util.InputMismatchException;

/**
 * 
 * This class implements insertion sort.
 *
 */

public class InsertionSorter extends AbstractSorter {
	// Other private instance variables if you need ...

	/**
	 * Constructor takes an array of points. It invokes the superclass constructor,
	 * and also set the instance variables algorithm in the superclass.
	 * 
	 * @param pts
	 */
	public InsertionSorter(Point[] pts) {
		super(pts);
		algorithm = "insertion Sort";
	}

	/**
	 * Perform insertion sort on the array points[] of the parent class
	 * AbstractSorter.
	 */
	@Override
	public void sort() {
		for (int i = 1; i < points.length; i++) {
			Point temp = points[i];
			int j = i - 1;
			while (j >= 0 && pointComparator.compare(points[j], temp) == 1) { // checks if point[j] is more than temp
				points[j + 1] = points[j];
				j=j-1;
			}
			points[j + 1] = temp;
		}
	}
}
