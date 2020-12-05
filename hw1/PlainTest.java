package edu.iastate.cs228.hw1;

/**
 * 
 * @author Brad Warren: bawarren@iastate.edu
 * 
 */
import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;

import org.junit.jupiter.api.Test;

/**
 * Plain class test cases
 */
class PlainTest {

	@Test
	void testPlainConstructor() throws FileNotFoundException {
		Plain p = new Plain("public1-3x3.txt");
		assertEquals(State.GRASS, p.grid[0][0].who());
	}

	@Test
	void testPlainConstructor2() {
		Plain p = new Plain(3);
		p.grid[1][1] = new Badger(p, 1, 1, 0);
		assertEquals(State.BADGER, p.grid[1][1].who());
	}

	@Test
	void testRandom() {
		Plain p = new Plain(3);
		p.randomInit();
		String one = p.toString();
		Plain p1 = new Plain(3);
		p1.randomInit();
		String two = p1.toString();
		assertEquals(false, one == two);

	}

	@Test
	void testWrite() throws FileNotFoundException {
		Plain p = new Plain("public1-3x3.txt");
		p.write("write.txt");
		Plain pn = new Plain("write.txt");
		assertEquals(State.GRASS, pn.grid[0][0].who());
	}

}
