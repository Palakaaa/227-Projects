package edu.iastate.cs228.hw2;

/**
 *  
 * @author Brad Warren: bawarren@iastate.edu
 *
 */

/**
 * 
 * This class executes four sorting algorithms: selection sort, insertion sort, mergesort, and
 * quicksort, over randomly generated integers as well integers from a file input. It compares the 
 * execution times of these algorithms on the same input. 
 *
 */

import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Arrays;
import java.util.Random;

public class CompareSorters {
	/**
	 * Repeatedly take integer sequences either randomly generated or read from
	 * files. Use them as coordinates to construct points. Scan these points with
	 * respect to their median coordinate point four times, each time using a
	 * different sorting algorithm.
	 * 
	 * @param args
	 **/
	public static void main(String[] args) throws FileNotFoundException {
		int trials = 1;
		System.out.println("Performances of Four Sorting Algorithms in Point Scanning");
		System.out.println("");
		System.out.println("keys: 1 (random integers) 2 (file input) 3 (exit)");
		Scanner sc = new Scanner(System.in);
		int input = sc.nextInt();
		while (input == 1 || input == 2) {
			RotationalPointScanner[] scanners = new RotationalPointScanner[4];
			while (input == 1) { // random points
				System.out.println("Trial" + " " + trials + ":" + input);
				System.out.println("Enter number of random points:");
				int numPoints = sc.nextInt();
				System.out.println("");
				System.out.println("algorithm" + "  " + "size" + "  " + "time (ns)");
				System.out.println("----------------------------------");
				Random rand = new Random();
				Point[] p = generateRandomPoints(numPoints, rand);
				scanners[0] = new RotationalPointScanner(p, Algorithm.SelectionSort);
				scanners[1] = new RotationalPointScanner(p, Algorithm.InsertionSort);
				scanners[2] = new RotationalPointScanner(p, Algorithm.MergeSort);
				scanners[3] = new RotationalPointScanner(p, Algorithm.QuickSort);
				for (int i = 0; i < scanners.length; i++) {
					scanners[i].scan();
					scanners[i].draw();
					System.out.println(scanners[i].stats());
				}
				System.out.println("----------------------------------");
				input = sc.nextInt();
				trials = trials +1;
			}
			while (input == 2) { // points from a file
				System.out.println("Trial" + " " + trials + ":" + input);
				System.out.println("File name:");
				String fileName = sc.next();
				System.out.println("");
				System.out.println("algorithm" + "  " + "size" + "  " + "time (ns)");
				System.out.println("----------------------------------");
				scanners[0] = new RotationalPointScanner(fileName, Algorithm.SelectionSort);
				scanners[1] = new RotationalPointScanner(fileName, Algorithm.InsertionSort);
				scanners[2] = new RotationalPointScanner(fileName, Algorithm.MergeSort);
				scanners[3] = new RotationalPointScanner(fileName, Algorithm.QuickSort);
				for (int i = 0; i < scanners.length; i++) {
					scanners[i].scan();
					scanners[i].draw();
					System.out.println(scanners[i].stats());
				}
				System.out.println("----------------------------------");
				input = sc.nextInt();
				trials = trials +1;
			}
		}
		sc.close();
	}

	/**
	 * This method generates a given number of random points. The coordinates of
	 * these points are pseudo-random numbers within the range [-50,50] × [-50,50].
	 * Please refer to Section 3 on how such points can be generated.
	 * 
	 * Ought to be private. Made public for testing.
	 * 
	 * @param numPts number of points
	 * @param rand   Random object to allow seeding of the random number generator
	 * @throws IllegalArgumentException if numPts < 1
	 */
	public static Point[] generateRandomPoints(int numPts, Random rand) throws IllegalArgumentException {
		Point[] result = new Point[numPts];
		for (int i = 0; i < numPts; i++) {
			result[i] = new Point((rand.nextInt(101) - 50), (rand.nextInt(101) - 50));
		}
		return result;
	}

}
