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
 * Wildlife class test case
 */
class WildlifeTest {

	@Test
	void testUpdatePlain() throws FileNotFoundException {
		Plain p = new Plain(3);
		Plain p2 = new Plain("GrassCase1");
		Wildlife.updatePlain(p2, p);
		assertEquals(State.EMPTY, p.grid[2][0].who());
	}

//	@Test
//	void testMain() {
//		
//	}
//	Tested when you run the main

}
