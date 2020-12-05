package edu.iastate.cs228.hw1;

/**
 *  
 * @author Brad Warren: bawarren@iastate.edu
 *
 */

/**
 * Grass remains if more than rabbits in the neighborhood; otherwise, it is
 * eaten.
 *
 */
public class Grass extends Living {
	public Grass(Plain p, int r, int c) {
		plain = p;
		row = r;
		column = c;
	}

	public State who() {
		return State.GRASS;
	}

	/**
	 * Grass can be eaten out by too many rabbits. Rabbits may also multiply fast
	 * enough to take over Grass.
	 */
	public Living next(Plain pNew) {
		int[] p = new int[NUM_LIFE_FORMS];
		census(p);
		if (p[RABBIT] >= (p[GRASS] * 3)) {
			return new Empty(pNew, row, column);
		} else if (p[RABBIT] >= 3) {
			return new Rabbit(pNew, row, column, 0);
		} else {
			return new Grass(pNew, row, column);
		}
	}
}
