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
 * Rabbit class test cases
 */
class RabbitTest {

	@Test
	void testCase1And2() throws FileNotFoundException {
		Plain p = new Plain("RabbitCase1And2");
		Rabbit r = (Rabbit) p.grid[0][1];
		Rabbit r2 = (Rabbit) p.grid[2][2];
		assertEquals(State.EMPTY, r.next(p).who());
		assertEquals(State.EMPTY, r2.next(p).who());
	}

	@Test
	void testCase3() throws FileNotFoundException {
		Plain p = new Plain("RabbitCase3And4");
		Rabbit r = (Rabbit) p.grid[1][1];
		Rabbit r2 = (Rabbit) p.grid[5][5];
		assertEquals(State.FOX, r.next(p).who());
		assertEquals(State.BADGER, r2.next(p).who());
	}
}
