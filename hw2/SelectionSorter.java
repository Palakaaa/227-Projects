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
 * This class implements selection sort.
 *
 */

public class SelectionSorter extends AbstractSorter {
	// Other private instance variables if you need ...

	/**
	 * Constructor takes an array of points. It invokes the superclass constructor,
	 * and also set the instance variables algorithm in the superclass.
	 * 
	 * @param pts
	 */
	public SelectionSorter(Point[] pts) {
		super(pts);
		algorithm = "selection sort";
	}

	/**
	 * Apply selection sort on the array points[] of the parent class
	 * AbstractSorter.
	 * 
	 */
	@Override
	public void sort() {
		for (int i = 0; i < points.length - 1; i++) {
			int min = i; // index of minimum point
			for (int j = i + 1; j < points.length; j++) {
				if (pointComparator.compare(points[j], points[min]) < 0) { // checks if point[j] is less than point[min]
					min = j;
				}
			}
			swap(i, min);
		}
	}
}
