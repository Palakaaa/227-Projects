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
 * Empty class test cases
 */
class EmptyTest {

	@Test
	void testCase1() throws FileNotFoundException {
		Plain p = new Plain("public2-6x6.txt");
		Empty e = (Empty) p.grid[4][4];
		assertEquals(State.RABBIT, e.next(p).who());
	}

	@Test
	void testCase2() throws FileNotFoundException {
		Plain p = new Plain("public2-6x6.txt");
		Empty e = (Empty) p.grid[0][1];
		assertEquals(State.FOX, e.next(p).who());
	}

	@Test
	void testCase3() throws FileNotFoundException {
		Plain p = new Plain("public2-6x6.txt");
		Empty e = (Empty) p.grid[4][1];
		assertEquals(State.BADGER, e.next(p).who());
	}

	@Test
	void testCase4() throws FileNotFoundException {
		Plain p = new Plain("public3-10x10.txt");
		Empty e = (Empty) p.grid[3][4];
		assertEquals(State.GRASS, e.next(p).who());

	}
}
