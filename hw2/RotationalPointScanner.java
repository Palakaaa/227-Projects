package edu.iastate.cs228.hw2;

/**
 * 
 * @author Brad Warren: bawarren@iastate.edu 
 *
 */

import java.io.File; 
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * 
 * This class sorts all the points in an array by polar angle with respect to a
 * reference point whose x and y coordinates are respectively the medians of the
 * x and y coordinates of the original points.
 * 
 * It records the employed sorting algorithm as well as the sorting time for
 * comparison.
 *
 */
public class RotationalPointScanner {
	private Point[] points;

	private Point medianCoordinatePoint; // point whose x and y coordinates are respectively the medians of
											// the x coordinates and y coordinates of those points in the array
											// points[].
	private Algorithm sortingAlgorithm;

	protected String outputFileName; // "select.txt", "insert.txt", "merge.txt", or "quick.txt"

	protected long scanTime; // execution time in nanoseconds.

	/**
	 * This constructor accepts an array of points and one of the four sorting
	 * algorithms as input. Copy the points into the array points[]. Set
	 * outputFileName.
	 * 
	 * @param pts input array of points
	 * @throws IllegalArgumentException if pts == null or pts.length == 0.
	 */
	public RotationalPointScanner(Point[] pts, Algorithm algo) throws IllegalArgumentException {
		if (pts == null || pts.length == 0) {
			throw new IllegalArgumentException();
		}
		points = new Point[pts.length];
		for (int i = 0; i < pts.length; i++) {
			points[i] = new Point(pts[i]);
		}
		if (algo == Algorithm.SelectionSort) {
			outputFileName = "select.txt";
		}
		sortingAlgorithm = algo;
		if (algo == Algorithm.InsertionSort) {
			outputFileName = "insert.txt";
		}
		if (algo == Algorithm.MergeSort) {
			outputFileName = "merge.txt";
		}
		if (algo == Algorithm.QuickSort) {
			outputFileName = "quick.txt";
		}
		sortingAlgorithm = algo;
	}

	/**
	 * This constructor reads points from a file. Set outputFileName.
	 * 
	 * @param inputFileName
	 * @throws FileNotFoundException
	 * @throws InputMismatchException if the input file contains an odd number of
	 *                                integers
	 */
	protected RotationalPointScanner(String inputFileName, Algorithm algo)
			throws FileNotFoundException, InputMismatchException {
		File file = new File(inputFileName);
		if (!file.exists() || !file.isFile()) {
			throw new FileNotFoundException();
		}
		int length = 0; // length of the file
		Scanner scan = new Scanner(file);
		while (scan.hasNextInt()) { // adds up length
			scan.nextInt();
			length += 1;
		}
		if (length % 2 != 0) { // if length is odd, throws InputMismatchException
			scan.close();
			throw new InputMismatchException();
		}
		scan.close();
		Scanner scan1 = new Scanner(file);
		points = new Point[length / 2];
		int i = 0; // index
		while(scan1.hasNextInt()) {
			int x = scan1.nextInt();
			int y = scan1.nextInt();
			points[i] = new Point(x, y);
			i++;
		}
		scan1.close();
		if (algo == Algorithm.SelectionSort) {
			outputFileName = "select.txt";
		}
		sortingAlgorithm = algo;
		if (algo == Algorithm.InsertionSort) {
			outputFileName = "insert.txt";
		}
		if (algo == Algorithm.MergeSort) {
			outputFileName = "merge.txt";
		}
		if (algo == Algorithm.QuickSort) {
			outputFileName = "quick.txt";
		}
		sortingAlgorithm = algo;
	}

	/**
	 * Carry out three rounds of sorting using the algorithm designated by
	 * sortingAlgorithm as follows:
	 * 
	 * a) Sort points[] by the x-coordinate to get the median x-coordinate. b) Sort
	 * points[] again by the y-coordinate to get the median y-coordinate. c)
	 * Construct medianCoordinatePoint using the obtained median x- and
	 * y-coordinates. d) Sort points[] again by the polar angle with respect to
	 * medianCoordinatePoint.
	 * 
	 * Based on the value of sortingAlgorithm, create an object of SelectionSorter,
	 * InsertionSorter, MergeSorter, or QuickSorter to carry out sorting. Copy the
	 * sorting result back onto the array points[] by calling the method getPoints()
	 * in AbstractSorter.
	 * 
	 * @param algo
	 * @return
	 */
	public void scan() {
		AbstractSorter aSorter = null;
		if (sortingAlgorithm == Algorithm.SelectionSort) {
			aSorter = new SelectionSorter(points);
			outputFileName = "select.txt";
		}
		if (sortingAlgorithm == Algorithm.InsertionSort) {
			aSorter = new InsertionSorter(points);
			outputFileName = "insert.txt";
		}
		if (sortingAlgorithm == Algorithm.MergeSort) {
			aSorter = new MergeSorter(points);
			outputFileName = "merge.txt";
		}
		if (sortingAlgorithm == Algorithm.QuickSort) {
			aSorter = new QuickSorter(points);
			outputFileName = "quick.txt";
		}
		aSorter.setComparator(0); // sort by x
		long start = System.nanoTime(); // starting time before sorts
		aSorter.sort();
		aSorter.getPoints(points);
		long scanT = System.nanoTime() - start; // total time
		int medX = aSorter.getMedian().getX(); // median points number for x

		aSorter.setComparator(1); // sort by y
		start = System.nanoTime();
		aSorter.sort();
		aSorter.getPoints(points);
		scanT += System.nanoTime() - start;
		int medY = aSorter.getMedian().getY(); // median points number for y
		medianCoordinatePoint = new Point(medX, medY); // setting medianCoordinatePoint
		aSorter.setReferencePoint(medianCoordinatePoint);

		aSorter.setComparator(2); // sort by polarangle
		start = System.nanoTime();
		aSorter.sort();
		aSorter.getPoints(points);
		scanT += System.nanoTime() - start;
		scanTime = scanT;
	}

	/**
	 * Outputs performance statistics in the format:
	 * 
	 * <sorting algorithm> <size> <time>
	 * 
	 * For instance,
	 * 
	 * selection sort 1000 9200867
	 * 
	 * Use the spacing in the sample run in Section 2 of the project description.
	 */
	public String stats() {
		String algo = "";
		if (sortingAlgorithm == Algorithm.SelectionSort) {
			algo = "SelectionSort";
		}
		if (sortingAlgorithm == Algorithm.InsertionSort) {
			algo = "InsertionSort";
		}
		if (sortingAlgorithm == Algorithm.MergeSort) {
			algo = "MergeSort    ";
		}
		if (sortingAlgorithm == Algorithm.QuickSort) {
			algo = "QuickSort    ";
		}

		return algo + " " + points.length + " " + scanTime;
	}

	/**
	 * Write points[] after a call to scan(). When printed, the points will appear
	 * in order of polar angle with respect to medianCoordinatePoint with every
	 * point occupying a separate line. The x and y coordinates of the point are
	 * displayed on the same line with exactly one blank space in between.
	 */
	@Override
	public String toString() {
		scan();
		String result = "";
		for (int i = 0; i < points.length; i++) {
			int x = points[i].getX();
			int y = points[i].getY();
			result = result + "(" + x + "," + " " + y + ")";
			result = "\n";
		}
		return result;
	}

	/**
	 * 
	 * This method, called after scanning, writes point data into a file by
	 * outputFileName. The format of data in the file is the same as printed out
	 * from toString(). The file can help you verify the full correctness of a
	 * sorting result and debug the underlying algorithm.
	 * 
	 * @throws FileNotFoundException
	 */
	public void writePointsToFile() throws FileNotFoundException {
		scan();
		File file = new File(outputFileName);
		PrintWriter write = new PrintWriter(file);
		write.print(toString());
		write.close();
	}

	/**
	 * This method is called after each scan for visually check whether the result
	 * is correct. You just need to generate a list of points and a list of
	 * segments, depending on the value of sortByAngle, as detailed in Section 4.1.
	 * Then create a Plot object to call the method myFrame().
	 */
	public void draw() {
		int numSegs = 2 * points.length; // number of segments to draw
		Segment[] segments = new Segment[numSegs];
		for (int i = 0; i < points.length - 1; i++) {
			segments[i] = new Segment(points[i], points[i + 1]); // copies adjacent points into a segment
		}
		segments[points.length - 1] = new Segment(points[points.length - 1], points[0]); // copies last point and first
																							// point into a segment
		for (int i = 0; i < points.length; i++) {
			segments[points.length + i] = new Segment(medianCoordinatePoint, points[i]); // copies points into a segment
																							// with the
																							// medianCoordinatePoint
		}

		String sort = null;

		switch (sortingAlgorithm) {
		case SelectionSort:
			sort = "Selection Sort";
			break;
		case InsertionSort:
			sort = "Insertion Sort";
			break;
		case MergeSort:
			sort = "Mergesort";
			break;
		case QuickSort:
			sort = "Quicksort";
			break;
		default:
			break;
		}

		// The following statement creates a window to display the sorting result.
		Plot.myFrame(points, segments, sort);

	}

}
