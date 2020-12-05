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
 * Grass class test cases
 */
class GrassTest {

	@Test
	void testCase1() throws FileNotFoundException {
		Plain p = new Plain("GrassCase1");
		Grass g = (Grass) p.grid[2][0];
		assertEquals(State.EMPTY, g.next(p).who());
	}

	@Test
	void testCase2() throws FileNotFoundException {
		Plain p = new Plain("GrassCase2");
		Grass g = (Grass) p.grid[1][1];
		assertEquals(State.RABBIT, g.next(p).who());
	}
}
