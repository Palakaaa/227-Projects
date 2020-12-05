package edu.iastate.cs228.hw5;

import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 *  
 * @author Brad Warren: bawarren@iastate.edu
 *
 */

/**
 * 
 * The Transactions class simulates video transactions at a video store.
 *
 */
public class Transactions {

	/**
	 * The main method generates a simulation of rental and return activities.
	 * 
	 * @param args
	 * @throws FileNotFoundException
	 * @throws AllCopiesRentedOutException
	 * @throws FilmNotInInventoryException
	 * @throws IllegalArgumentException
	 */
	public static void main(String[] args) throws FileNotFoundException, IllegalArgumentException,
			FilmNotInInventoryException, AllCopiesRentedOutException {
		Scanner scan = new Scanner(System.in);
		String fileName = scan.next();
		VideoStore store = new VideoStore(fileName);
		// Adds required messages
		System.out.println("Transactions at a Video Store");
		System.out.println("keys: 1 (rent)      2 (bulk rent)");
		System.out.println("      3 (return)    4 (bulk return)");
		System.out.println("      5 (summary)   6 (exit)\n");
		System.out.println("Transaction:");
		int input = scan.nextInt(); // Determines first transaction
		while (input != 6) { // Closes program is input is 6

			if (input == 1) { // If input is 1
				System.out.println("Film to rent:");
				String film = scan.next();
				film += scan.nextLine();
				Video video = new Video(store.parseFilmName(film), store.parseNumCopies(film));
				// Controls exceptions and prints messages according to exceptions that occur
				try {
					store.videoRent(video.getFilm(), video.getNumCopies()); // Rents film from store
				} catch (IllegalArgumentException e) {
					System.out.println(e.getMessage());
				} catch (FilmNotInInventoryException e) {
					System.out.println(e.getMessage());
				} catch (AllCopiesRentedOutException e) {
					System.out.println(e.getMessage());
				}
			}
			if (input == 2) { // If input is 2
				System.out.println("Video file (rent):");
				String file = scan.next();
				// Controls exceptions and prints messages according to exceptions that occur
				try {
					store.bulkRent(file); // Rents films from file
				} catch (IllegalArgumentException e) {
					System.out.println(e.getMessage());
				} catch (FilmNotInInventoryException e) {
					System.out.println(e.getMessage());
				} catch (AllCopiesRentedOutException e) {
					System.out.println(e.getMessage());
				}
			}
			if (input == 3) { // If input is 3
				System.out.println("Film to return:");
				String film = scan.next();
				film += scan.nextLine();
				Video video = new Video(store.parseFilmName(film), store.parseNumCopies(film));
				// Controls exceptions and prints messages according to exceptions that occur
				try {
					store.videoReturn(video.getFilm(), video.getNumCopies()); // Returns film to store
				} catch (IllegalArgumentException e) {
					System.out.println(e.getMessage());
				} catch (FilmNotInInventoryException e) {
					System.out.println(e.getMessage());
				}
			}
			if (input == 4) { // If input is 4
				System.out.println("Video file (return):");
				String file = scan.next();
				// Controls exceptions and prints messages according to exceptions that occur
				try {
					store.bulkReturn(file); // Returns films from file
				} catch (IllegalArgumentException e) {
					System.out.println(e.getMessage());
				} catch (FilmNotInInventoryException e) {
					System.out.println(e.getMessage());
				}
			}
			if (input == 5) { // If input is 5
				System.out.println(store.transactionsSummary()); // Gives summary of store
			}
			System.out.println("\nTransaction:"); // Starts new transaction
			input = scan.nextInt(); // Determines type of next transaction

		}
		scan.close();
	}
}
