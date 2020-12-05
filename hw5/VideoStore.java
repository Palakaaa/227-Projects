package edu.iastate.cs228.hw5;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Scanner;
import edu.iastate.cs228.hw5.SplayTree.Node;

/**
 * 
 * @author Brad Warren: bawarren@iastate.edu
 *
 */

public class VideoStore {
	protected SplayTree<Video> inventory; // all the videos at the store

	// ------------
	// Constructors
	// ------------

	/**
	 * Default constructor sets inventory to an empty tree.
	 */
	public VideoStore() {
		// no need to implement.
	}

	/**
	 * Constructor accepts a video file to create its inventory. Refer to Section
	 * 3.2 of the project description for details regarding the format of a video
	 * file.
	 * 
	 * Calls setUpInventory().
	 * 
	 * @param videoFile no format checking on the file
	 * @throws FileNotFoundException
	 */
	public VideoStore(String videoFile) throws FileNotFoundException {
		inventory = new SplayTree<Video>();
		setUpInventory(videoFile);
	}

	/**
	 * Accepts a video file to initialize the splay tree inventory. To be efficient,
	 * add videos to the inventory by calling the addBST() method, which does not
	 * splay.
	 * 
	 * Refer to Section 3.2 for the format of video file.
	 * 
	 * @param videoFile correctly formated if exists
	 * @throws FileNotFoundException
	 */
	public void setUpInventory(String videoFile) throws FileNotFoundException {
		if (inventory != null) {
			inventory.clear();
		}
		bulkImport(videoFile); // Add videos to the inventory by calling the addBST() method
	}

	// ------------------
	// Inventory Addition
	// ------------------

	/**
	 * Find a Video object by film title.
	 * 
	 * @param film
	 * @return
	 */
	public Video findVideo(String film) {
		return inventory.findElement(new Video(film));
	}

	/**
	 * Updates the splay tree inventory by adding a number of video copies of the
	 * film. (Splaying is justified as new videos are more likely to be rented.)
	 * 
	 * Calls the add() method of SplayTree to add the video object.
	 * 
	 * a) If true is returned, the film was not on the inventory before, and has
	 * been added. b) If false is returned, the film is already on the inventory.
	 * 
	 * The root of the splay tree must store the corresponding Video object for the
	 * film. Update the number of copies for the film.
	 * 
	 * @param film title of the film
	 * @param n    number of video copies
	 */
	public void addVideo(String film, int n) {
		Video video = new Video(film, n);
		boolean result = inventory.add(video); // Calls splaytree add method
		if (result == false) { // Film is already in inventory
			Video vid = inventory.findElement(video);
			vid.addNumCopies(n);
		}
	}

	/**
	 * Add one video copy of the film.
	 * 
	 * @param film title of the film
	 */
	public void addVideo(String film) {
		addVideo(film, 1);
	}

	/**
	 * Update the splay trees inventory by adding videos. Perform binary search
	 * additions by calling addBST() without splaying.
	 * 
	 * The videoFile format is given in Section 3.2 of the project description.
	 * 
	 * @param videoFile correctly formated if exists
	 * @throws FileNotFoundException
	 */
	public void bulkImport(String videoFile) throws FileNotFoundException {
		File file = new File(videoFile);
		Scanner scan = new Scanner(file);
		while (scan.hasNextLine()) { // Iterates through file
			String data = scan.next();
			data += scan.nextLine();
			String name = parseFilmName(data);
			int copies = parseNumCopies(data);
			inventory.addBST(new Video(name, copies)); // Adds video to inventory
		}
		scan.close();
	}

	// ----------------------------
	// Video Query, Rental & Return
	// ----------------------------

	/**
	 * Search the splay tree inventory to determine if a video is available.
	 * 
	 * @param film
	 * @return true if available
	 */
	public boolean available(String film) {
		if (inventory.findElement(new Video(film)).getNumAvailableCopies() > 0) { // Determines if the film is
																					// available
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Update inventory.
	 * 
	 * Search if the film is in inventory by calling findElement(new Video(film,
	 * 1)).
	 * 
	 * If the film is not in inventory, prints the message "Film <film> is not in
	 * inventory", where <film> shall be replaced with the string that is the value
	 * of the parameter film. If the film is in inventory with no copy left, prints
	 * the message "Film <film> has been rented out".
	 * 
	 * If there is at least one available copy but n is greater than the number of
	 * such copies, rent all available copies. In this case, no
	 * AllCopiesRentedOutException is thrown.
	 * 
	 * @param film
	 * @param n
	 * @throws IllegalArgumentException    if n <= 0 or film == null or
	 *                                     film.isEmpty()
	 * @throws FilmNotInInventoryException if film is not in the inventory
	 * @throws AllCopiesRentedOutException if there is zero available copy for the
	 *                                     film.
	 */
	public void videoRent(String film, int n)
			throws IllegalArgumentException, FilmNotInInventoryException, AllCopiesRentedOutException {
		if (n <= 0 || film == null || film.isEmpty() == true) {
			throw new IllegalArgumentException("Film " + film + " does not exist or number request invalid");
		}
		Video video = inventory.findElement(new Video(film, n)); // Finds film
		if (video == null) {
			throw new FilmNotInInventoryException("Film " + film + " is not in inventory");
		} else if (video.getNumRentedCopies() == video.getNumCopies()) {
			throw new AllCopiesRentedOutException("Film " + film + " has been rented out");
		} else {
			if (n >= video.getNumAvailableCopies()) {
				video.rentCopies(video.getNumAvailableCopies());
			} else {
				video.rentCopies(n);
			}
		}
	}

	/**
	 * Update inventory.
	 * 
	 * 1. Calls videoRent() repeatedly for every video listed in the file. 2. For
	 * each requested video, do the following: a) If it is not in inventory or is
	 * rented out, an exception will be thrown from videoRent(). Based on the
	 * exception, prints out the following message: "Film <film> is not in
	 * inventory" or "Film <film> has been rented out." In the message, <film> shall
	 * be replaced with the name of the video. b) Otherwise, update the video record
	 * in the inventory.
	 * 
	 * For details on handling of multiple exceptions and message printing, please
	 * read Section 3.4 of the project description.
	 * 
	 * @param videoFile correctly formatted if exists
	 * @throws FileNotFoundException
	 * @throws IllegalArgumentException    if the number of copies of any film is <=
	 *                                     0
	 * @throws FilmNotInInventoryException if any film from the videoFile is not in
	 *                                     the inventory
	 * @throws AllCopiesRentedOutException if there is zero available copy for some
	 *                                     film in videoFile
	 */
	public void bulkRent(String videoFile) throws FileNotFoundException, IllegalArgumentException,
			FilmNotInInventoryException, AllCopiesRentedOutException {
		boolean[] errors = new boolean[3]; // Array to determine if exception is thrown
		String error = ""; // String of errors that occur
		File file = new File(videoFile);
		Scanner scan = new Scanner(file);
		while (scan.hasNextLine()) {
			String film = scan.next();
			film += scan.nextLine();
			Video video = new Video(parseFilmName(film), parseNumCopies(film)); // Create film from string
			try {
				videoRent(video.getFilm(), video.getNumCopies());
			} catch (IllegalArgumentException e) { // Adds error to string of errors if caught and updates array boolean
													// value according to exception
				error += e.getMessage() + "\n";
				errors[0] = true;
			} catch (FilmNotInInventoryException e) { // Adds error to string of errors if caught and updates array
														// boolean value according to exception
				error += e.getMessage() + "\n";
				errors[1] = true;
			} catch (AllCopiesRentedOutException e) { // Adds error to string of errors if caught and updates array
														// boolean value according to exception
				error += e.getMessage() + "\n";
				errors[2] = true;
			}
		}
		scan.close();
		// Throws exception in order of importance if boolean variable that corresponds
		// with the error is true

		if (errors[0] == true) { // Throws exception and string of all exceptions that occurred
			throw new IllegalArgumentException(error);
		} else if (errors[1] == true) { // Throws exception and string of all exceptions that occurred
			throw new FilmNotInInventoryException(error);
		} else if (errors[2] == true) { // Throws exception and string of all exceptions that occurred
			throw new AllCopiesRentedOutException(error);
		}
	}

	/**
	 * Update inventory.
	 * 
	 * If n exceeds the number of rented video copies, accepts up to that number of
	 * rented copies while ignoring the extra copies.
	 * 
	 * @param film
	 * @param n
	 * @throws IllegalArgumentException    if n <= 0 or film == null or
	 *                                     film.isEmpty()
	 * @throws FilmNotInInventoryException if film is not in the inventory
	 */
	public void videoReturn(String film, int n) throws IllegalArgumentException, FilmNotInInventoryException {
		if (n <= 0 || film == null || film.isEmpty() == true) {
			throw new IllegalArgumentException("Film " + film + " does not exist or number request invalid");
		}
		Video video = inventory.findElement(new Video(film, n)); // Finds film
		if (video == null) {
			throw new FilmNotInInventoryException("Film " + film + " is not in inventory");
		} else {
			if (n > video.getNumRentedCopies()) {
				video.returnCopies(video.getNumCopies());
			} else {
				video.returnCopies(n);
			}
		}
	}

	/**
	 * Update inventory.
	 * 
	 * Handles excessive returned copies of a film in the same way as videoReturn()
	 * does. See Section 3.4 of the project description on how to handle multiple
	 * exceptions.
	 * 
	 * @param videoFile
	 * @throws FileNotFoundException
	 * @throws IllegalArgumentException    if the number of return copies of any
	 *                                     film is <= 0
	 * @throws FilmNotInInventoryException if a film from videoFile is not in
	 *                                     inventory
	 */
	public void bulkReturn(String videoFile)
			throws FileNotFoundException, IllegalArgumentException, FilmNotInInventoryException {
		boolean[] errors = new boolean[2]; // Array to determine if exception is thrown
		String error = ""; // String of errors that occur
		File file = new File(videoFile);
		Scanner scan = new Scanner(file);
		while (scan.hasNextLine()) {
			String film = scan.next();
			film += scan.nextLine();
			Video video = new Video(parseFilmName(film), parseNumCopies(film)); // Create film from string
			try {
				videoReturn(video.getFilm(), video.getNumCopies()); // Trys to return the video
			} catch (IllegalArgumentException e) {// Adds error to string of errors if caught and updates array boolean
													// value according to exception
				error += e.getMessage() + "\n";
				errors[0] = true;
			} catch (FilmNotInInventoryException e) {// Adds error to string of errors if caught and updates array
														// boolean value according to exception
				error += e.getMessage() + "\n";
				errors[1] = true;
			}
		}
		scan.close();
		// Throws exception in order of importance if boolean variable that corresponds
		// with the error is true

		if (errors[0] == true) { // Throws exception and string of all exceptions that occurred
			throw new IllegalArgumentException(error);
		} else if (errors[1] == true) { // Throws exception and string of all exceptions that occurred
			throw new FilmNotInInventoryException(error);
		}
	}

	// ------------------------
	// Methods without Splaying
	// ------------------------

	/**
	 * Performs inorder traversal on the splay tree inventory to list all the videos
	 * by film title, whether rented or not. Below is a sample string if printed
	 * out:
	 * 
	 * 
	 * Films in inventory:
	 * 
	 * A Streetcar Named Desire (1) Brokeback Mountain (1) Forrest Gump (1) Psycho
	 * (1) Singin' in the Rain (2) Slumdog Millionaire (5) Taxi Driver (1) The
	 * Godfather (1)
	 * 
	 * 
	 * @return
	 */
	public String inventoryList() {
		Iterator<Video> iter = inventory.iterator(); // Creates splay tree iterator
		Node current = inventory.root;
		String videos = "";
		while (current.left != null) { // Traverses to the lowest node to the left
			current = current.left;
		}
		videos += "Films in inventory:\n"; // Adds required text
		while (iter.hasNext()) { // Traverses through splay tree
			Video vid = iter.next();
			String name = vid.getFilm();
			int copies = vid.getNumCopies();
			videos += name + " (" + copies + ") ";
		}

		return videos;
	}

	/**
	 * Calls rentedVideosList() and unrentedVideosList() sequentially. For the
	 * string format, see Transaction 5 in the sample simulation in Section 4 of the
	 * project description.
	 * 
	 * @return
	 */
	public String transactionsSummary() {
		return rentedVideosList() + "\n" + unrentedVideosList();
	}

	/**
	 * Performs inorder traversal on the splay tree inventory. Use a splay tree
	 * iterator.
	 * 
	 * Below is a sample return string when printed out:
	 * 
	 * Rented films:
	 * 
	 * Brokeback Mountain (1) Forrest Gump (1) Singin' in the Rain (2) The Godfather
	 * (1)
	 * 
	 * 
	 * @return
	 */
	private String rentedVideosList() {
		Iterator<Video> iter = inventory.iterator(); // Creates splay tree iterator
		Node current = inventory.root;
		String videos = "";
		while (current.left != null) { // Traverses to the lowest node to the left
			current = current.left;
		}
		videos += "Rented films:\n\n"; // Adds required text
		while (iter.hasNext()) { // Traverses through splay tree
			Video vid = iter.next();
			if (vid.getNumRentedCopies() != 0) { // If there are copies of film rented, we add to the string
				String name = vid.getFilm();
				int cops = vid.getNumRentedCopies();
				videos += name + " (" + cops + ")\n";
			}
		}
		return videos;
	}

	/**
	 * Performs inorder traversal on the splay tree inventory. Use a splay tree
	 * iterator. Prints only the films that have unrented copies.
	 * 
	 * Below is a sample return string when printed out:
	 * 
	 * 
	 * Films remaining in inventory:
	 * 
	 * A Streetcar Named Desire (1) Forrest Gump (1) Psycho (1) Slumdog Millionaire
	 * (4) Taxi Driver (1)
	 * 
	 * 
	 * @return
	 */
	private String unrentedVideosList() {
		Iterator<Video> iter = inventory.iterator(); // Creates splay tree iterator
		Node current = inventory.root;
		String videos = "";
		while (current.left != null) { // Traverses to lowest node to the left
			current = current.left;
		}
		videos += "Films remaining in inventory:\n\n"; // Adds required text
		while (iter.hasNext()) { // Traverses through splay tree
			Video v = iter.next();
			if (v.getNumAvailableCopies() != 0) { // If there are copies of the film, we add it to the string
				String name = v.getFilm();
				int copies = v.getNumAvailableCopies();
				videos += name + " (" + copies + ")\n";
			}
		}
		return videos;
	}

	/**
	 * Parse the film name from an input line.
	 * 
	 * @param line
	 * @return
	 */
	public static String parseFilmName(String line) {
		Scanner scan = new Scanner(line);
		String result = "";
		while (scan.hasNext()) { // Traverses through string
			String index = scan.next();
			if (index.charAt(0) != '(') { // If string is not the string containing the number
											// of copies
				result += index + " ";
			}
		}
		scan.close();
		return result;
	}

	/**
	 * Parse the number of copies from an input line.
	 * 
	 * @param line
	 * @return
	 */
	public static int parseNumCopies(String line) {
		Scanner scan = new Scanner(line);
		int copies = 1;
		int count = 1;
		String value = "";
		while (scan.hasNext()) { // Traverses through string
			String index = scan.next();
			if (index.charAt(0) == '(') { // If string contains the number of copies
				while (index.charAt(count) != ')') { // Adds number to value until we reach end of the number of copies
					char num = index.charAt(count);
					value += num;
					++count;
				}
			}
		}
		if (value != "") { // If value is updated then we update copies to be equal to value
			copies = Integer.parseInt(value);
		}
		scan.close();
		if (copies <= 0) { // If copies if <= 0 than return 1
			return 1;
		} else {
			return copies;
		}
	}
}
