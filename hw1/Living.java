package edu.iastate.cs228.hw1;

/**
 *  
 * @author Brad Warren: bawarren@iastate.edu
 *
 */

/**
 * 
 * Living refers to the life form occupying a square in a plain grid. It is a
 * superclass of Empty, Grass, and Animal, the latter of which is in turn a
 * superclass of Badger, Fox, and Rabbit. Living has two abstract methods
 * awaiting implementation.
 *
 */
public abstract class Living {
	protected Plain plain; // the plain in which the life form resides
	protected int row; // location of the square on which
	protected int column; // the life form resides

	// constants to be used as indices.
	protected static final int BADGER = 0;
	protected static final int EMPTY = 1;
	protected static final int FOX = 2;
	protected static final int GRASS = 3;
	protected static final int RABBIT = 4;

	public static final int NUM_LIFE_FORMS = 5;

	// life expectancies
	public static final int BADGER_MAX_AGE = 4;
	public static final int FOX_MAX_AGE = 6;
	public static final int RABBIT_MAX_AGE = 3;

	/**
	 * Censuses all life forms in the 3 X 3 neighborhood in a plain.
	 * 
	 * @param population counts of all life forms
	 */
	protected void census(int population[]) {

		if (row == 0 && column == 0) {
			// set up 2x2 grid top left
			for (int i = row; i <= 1; i++) {
				for (int j = column; j <= 1; j++) {
					censusHelper(population, i, j);
				}
			}
		} else if (row == plain.grid.length - 1 && column == 0) {
			// set up 2x2 grid bottom left
			for (int i = plain.grid.length - 2; i <= plain.grid.length - 1; i++) {
				for (int j = 0; j <= 1; j++) {
					censusHelper(population, i, j);
				}
			}
		} else if (row == plain.grid.length - 1 && column == plain.grid[0].length - 1) {
			// set up 2x2 grid bottom right
			for (int i = plain.grid.length - 2; i <= plain.grid.length - 1; i++) {
				for (int j = plain.grid[0].length - 2; j <= plain.grid[0].length - 1; j++) {
					censusHelper(population, i, j);
				}
			}
		} else if (row == 0 && column == plain.grid[0].length - 1) {
			// set up 2x2 grid top right
			for (int i = row; i <= 1; i++) {
				for (int j = plain.grid[0].length - 2; j <= plain.grid[0].length - 1; j++) {
					censusHelper(population, i, j);
				}
			}
		} else if (column == 0 && (row >= 1 && row < plain.grid.length - 1)) {
			// 3x2 Left side
			for (int i = row - 1; i <= row + 1; i++) {
				for (int j = 0; j <= 1; j++) {
					censusHelper(population, i, j);
				}
			}
		} else if (column == plain.grid[0].length - 1 && row >= 1 && row < plain.grid.length - 1) {
			// 3x2 Right side
			for (int i = row - 1; i <= row + 1; i++) {
				for (int j = plain.grid[0].length - 2; j <= plain.grid[0].length - 1; j++) {
					censusHelper(population, i, j);
				}
			}
		} else if (row == 0 && (column >= 1 || column < plain.grid[0].length - 1)) {
			// 2x3 Top side
			for (int i = row; i <= 1; i++) {
				for (int j = column - 1; j <= column + 1; j++) {
					censusHelper(population, i, j);
				}
			}
		} else if (row == plain.grid.length - 1 && (column >= 1 || column < plain.grid[0].length - 1)) {
			// 2x3 Bottom side
			for (int i = plain.grid.length - 2; i <= plain.grid.length - 1; i++) {
				for (int j = column - 1; j <= column + 1; j++) {
					censusHelper(population, i, j);
				}
			}
		} else {
			// 3x3
			for (int i = row - 1; i <= row + 1; i++) {
				for (int j = column - 1; j <= column + 1; j++) {
					censusHelper(population, i, j);
				}
			}
		}
	}

	public void censusHelper(int[] p, int i, int j) {
		if (plain.grid[i][j].who().equals(State.BADGER)) {
			p[0] = p[0] + 1;
		} else if (plain.grid[i][j].who().equals(State.EMPTY)) {
			p[1] = p[1] + 1;

		} else if (plain.grid[i][j].who().equals(State.FOX)) {
			p[2] = p[2] + 1;
		} else if (plain.grid[i][j].who().equals(State.GRASS)) {
			p[3] = p[3] + 1;
		} else if(plain.grid[i][j].who().equals(State.RABBIT)){
			p[4] = p[4] + 1;
		}
	}

	/**
	 * Gets the identity of the life form on the square.
	 * 
	 * @return State
	 */
	public abstract State who();
	// To be implemented in each class of Badger, Empty, Fox, Grass, and Rabbit.
	//
	// There are five states given in State.java. Include the prefix State in
	// the return value, e.g., return State.Fox instead of Fox.

	/**
	 * Determines the life form on the square in the next cycle.
	 * 
	 * @param pNew plain of the next cycle
	 * @return Living
	 */
	public abstract Living next(Plain pNew);
	// To be implemented in the classes Badger, Empty, Fox, Grass, and Rabbit.
	//
	// For each class (life form), carry out the following:
	//
	// 1. Obtains counts of life forms in the 3x3 neighborhood of the class object.

	// 2. Applies the survival rules for the life form to determine the life form
	// (on the same square) in the next cycle. These rules are given in the
	// project description.
	//
	// 3. Generate this new life form at the same location in the plain pNew.

}
