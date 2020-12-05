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
 * Fox class test cases
 */
class FoxTest {

	@Test
	void testCase1() {
		Plain p = new Plain(3);
		Fox f = new Fox(p, 0, 0, 6);
		assertEquals(State.EMPTY, f.next(p).who());
	}

	//TODO
	@Test
	void testCase2() throws FileNotFoundException {
		Plain p = new Plain("FoxCase2And3");
		Fox f = (Fox) p.grid[1][1];
		Fox f2 = (Fox) p.grid[4][4];
		assertEquals(State.BADGER, f.next(p).who());
		assertEquals(State.EMPTY, f2.next(p).who());
	}
}
