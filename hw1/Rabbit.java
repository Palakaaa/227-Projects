package edu.iastate.cs228.hw1;

/**
 * 
 * @author Brad Warren: bawarren@iastate.edu
 *
 */

/*
 * A rabbit eats grass and lives no more than three years.
 */
public class Rabbit extends Animal {
	/**
	 * Creates a Rabbit object.
	 * 
	 * @param p: plain
	 * @param r: row position
	 * @param c: column position
	 * @param a: age
	 */
	public Rabbit(Plain p, int r, int c, int a) {
		super(p, r, c, a);
	}

	/**
	 * Rabbit occupies the square.
	 */
	public State who() {
		return State.RABBIT;
	}

	/**
	 * A rabbit dies of old age or hunger. It may also be eaten by a badger or a
	 * fox.
	 * 
	 * @param pNew plain of the next cycle
	 * @return Living new life form occupying the same square
	 */
	public Living next(Plain pNew) {
		int[] p = new int[NUM_LIFE_FORMS];
		census(p);
		if (myAge() == RABBIT_MAX_AGE) {
			return new Empty(pNew, row, column);
		} else if (p[GRASS] == 0) {
			return new Empty(pNew, row, column);
		} else if (((p[FOX] + p[BADGER]) >= p[RABBIT]) && (p[BADGER] < p[FOX])) {
			return new Fox(pNew, row, column, 0);
		} else if (p[BADGER] > p[RABBIT]) {
			return new Badger(pNew, row, column, 0);
		} else {
			return new Rabbit(pNew, row, column, myAge() + 1);
		}
	}
}
