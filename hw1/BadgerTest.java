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
 * Badger class test cases
 */
class BadgerTest {

	@Test
	void testCase1() throws FileNotFoundException {
		Plain p = new Plain("public3-10x10.txt");
		Badger b = (Badger) p.grid[1][7];
		assertEquals(State.EMPTY, b.next(p).who());
	}

	@Test
	void testCase2() throws FileNotFoundException {
		Plain p = new Plain("public2-6x6.txt");
		Badger b = (Badger) p.grid[1][0];
		assertEquals(State.FOX, b.next(p).who());
	}

	@Test
	void testCase3() throws FileNotFoundException {
		Plain p = new Plain("public2-6x6.txt");
		Badger b = (Badger) p.grid[1][2];
		assertEquals(State.EMPTY, b.next(p).who());
	}
}
