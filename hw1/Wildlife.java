package edu.iastate.cs228.hw1;

import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 *  
 * @author Brad Warren: bawarren@iastate.edu
 *
 */

/**
 * 
 * The Wildlife class performs a simulation of a grid plain with squares
 * inhabited by badgers, foxes, rabbits, grass, or none.
 *
 */
public class Wildlife {
	/**
	 * Update the new plain from the old plain in one cycle.
	 * 
	 * @param pOld old plain
	 * @param pNew new plain
	 */
	public static void updatePlain(Plain pOld, Plain pNew) {
		for (int i = 0; i < pOld.grid.length; i++) {
			for (int j = 0; j < pNew.grid[0].length; j++) {
				pNew.grid[i][j] = pOld.grid[i][j].next(pNew);
			}
		}
	}

	/**
	 * Repeatedly generates plains either randomly or from reading files. Over each
	 * plain, carries out an input number of cycles of evolution.
	 * 
	 * @param args
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException {
		boolean proceed = true;
		int trials = 0;
		System.out.println("Simulation of Wildlife of the Plain");
		System.out.println("keys: 1 (random plain)  2 (file input)  3(exit)");
		Scanner sc = new Scanner(System.in);
		int input = sc.nextInt();
		while (proceed == true) {

			if (input != 1 || input != 2 || input != 0) {
				proceed = false;
			}
			while (input == 1) {
				trials = trials + 1;
				System.out.println("Trial" + " " + trials + ":" + input);
				System.out.println("Random Plain");
				System.out.println("Enter grid width:");
				int w = sc.nextInt();
				while (w < 2) {
					w = sc.nextInt();
				}
				System.out.println("Enter number of cycles:");
				int c = sc.nextInt();
				while (c < 1) {
					c = sc.nextInt();
				}
				System.out.println(" ");
				System.out.println("Initial plain");
				Plain even = new Plain(w);
				Plain odd = new Plain(w);
				even.randomInit();
				System.out.println(even.toString());
				System.out.println(" ");
				int cycles = 0;
				while (cycles != c) {
					if (cycles < c) {
						updatePlain(even, odd);
						cycles++;
					}
					if (cycles != c) {
						updatePlain(odd, even);
						cycles++;
					}
				}
				System.out.println("Final plain:");
				System.out.println(" ");
				if (cycles % 2 == 0) {
					System.out.println(even.toString());
				}
				if (cycles % 2 != 0) {
					System.out.println(odd.toString());
				}
				input = sc.nextInt();

			}
			while (input == 2) {
				trials = trials + 1;
				System.out.println("Trial" + " " + trials + ":" + input);
				System.out.println("Plain input from a file");
				System.out.println("File name:");
				String file = sc.next();
				System.out.println("Enter number of cycles:");
				int c = sc.nextInt();
				while (c < 1) {
					c = sc.nextInt();
				}
				System.out.println(" ");
				System.out.println("Initial plain:");
				Plain even = new Plain(file);
				Plain odd = new Plain(file);
				System.out.println(" ");
				System.out.println(even.toString());
				System.out.println(" ");
				int cycles = 0;
				while (cycles != c) {
					if (cycles < c) {
						updatePlain(even, odd);
						System.out.println(odd.toString());
						cycles++;
					}
					if (cycles != c) {
						updatePlain(odd, even);
						System.out.println(even.toString());
						cycles++;
					}
				}
				System.out.println("Final plain:");
				System.out.println(" ");
				if (cycles % 2 == 0) {
					System.out.println(even.toString());
				}
				if (cycles % 2 != 0) {
					System.out.println(odd.toString());
				}
				input = sc.nextInt();
			}

		}
		sc.close();
	}
}
