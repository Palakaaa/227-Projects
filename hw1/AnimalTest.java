package edu.iastate.cs228.hw1;

/**
 * 
 * @author Brad Warren: bawarren@iastate.edu
 * 
 */
import static org.junit.jupiter.api.Assertions.*;
/**
 * @author Brad Warren: bawarren@iastate.edu
 */
import org.junit.jupiter.api.Test;

/*
 * Animal class test cases
 */
class AnimalTest {

	@Test
	void testBadgerAge() {
		Plain p = new Plain(3);
		Animal a = new Badger(p, 0, 0, 1);
		assertEquals(1, a.myAge());
	}

	@Test
	void testFoxAge() {
		Plain p = new Plain(3);
		Animal a = new Fox(p, 0, 0, 1);
		assertEquals(1, a.myAge());
	}

	@Test
	void testRabbitAge() {
		Plain p = new Plain(3);
		Animal a = new Rabbit(p, 0, 0, 1);
		assertEquals(1, a.myAge());
	}

}
