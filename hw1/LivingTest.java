package edu.iastate.cs228.hw1;

/**
 * 
 * @author Brad Warren: bawarren@iastate.edu
 * 
 */
import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;

import org.junit.jupiter.api.Test;

/*
 * Living class test cases
 */
class LivingTest {

	final int BADGER = 0;
	final int EMPTY = 1;
	final int FOX = 2;
	final int GRASS = 3;
	final int RABBIT = 4;

	@Test
	void testTopLeftCorner() throws FileNotFoundException {
		int[] population = new int[5];
		Plain p = new Plain("public1-3x3.txt");
		p.grid[0][0].census(population);
		assertEquals(1, population[BADGER]);
		assertEquals(2, population[FOX]);
	}

	@Test
	void testTopEdge() throws FileNotFoundException {
		int[] population = new int[5];
		Plain p = new Plain("public1-3x3.txt");
		p.grid[0][1].census(population);
		assertEquals(0, population[EMPTY]);
		assertEquals(3, population[FOX]);
		assertEquals(1, population[BADGER]);
		assertEquals(1, population[RABBIT]);
		assertEquals(1, population[GRASS]);

	}

	@Test
	void testTopRightCorner() throws FileNotFoundException {
		int[] population = new int[5];
		Plain p = new Plain("public3-10x10.txt");
		p.grid[0][9].census(population);
		assertEquals(1, population[EMPTY]);
		assertEquals(3, population[GRASS]);
	}

	@Test
	void testLeftEdge() throws FileNotFoundException {
		int[] population = new int[5];
		Plain p = new Plain("public3-10x10.txt");
		p.grid[1][0].census(population);
		assertEquals(2, population[EMPTY]);
		assertEquals(3, population[GRASS]);
		assertEquals(1, population[BADGER]);
	}

	@Test
	void testBottomLeftCorner() throws FileNotFoundException {
		int[] population = new int[5];
		Plain p = new Plain("public3-10x10.txt");
		p.grid[9][0].census(population);
		assertEquals(3, population[GRASS]);
		assertEquals(1, population[EMPTY]);
	}

	@Test
	void testBottomEdge() throws FileNotFoundException {
		int[] population = new int[5];
		Plain p = new Plain("public3-10x10.txt");
		p.grid[9][2].census(population);
		assertEquals(2, population[EMPTY]);
		assertEquals(3, population[GRASS]);
		assertEquals(1, population[FOX]);
	}

	@Test
	void testBottomRightCorner() throws FileNotFoundException {
		int[] population = new int[5];
		Plain p = new Plain("public3-10x10.txt");
		p.grid[9][9].census(population);
		assertEquals(4, population[GRASS]);
	}

	@Test
	void testRightEdge() throws FileNotFoundException {
		int[] population = new int[5];
		Plain w = new Plain("public3-10x10.txt");
		w.grid[8][9].census(population);
		assertEquals(5, population[GRASS]);
		assertEquals(1, population[EMPTY]);
	}

	@Test
	void testStandardGroup() throws FileNotFoundException {
		int[] population = new int[5];
		Plain p = new Plain("public3-10x10.txt");
		p.grid[1][1].census(population);
		assertEquals(2, population[EMPTY]);
		assertEquals(4, population[GRASS]);
		assertEquals(3, population[BADGER]);
	}

}
