package edu.iastate.cs228.hw3;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *  
 * @author Brad Warren: bawarren@iastate.edu
 *
 */

import java.util.ListIterator;

public class PrimeFactorization implements Iterable<PrimeFactor> {
	private static final long OVERFLOW = -1;
	private long value; // the factored integer
						// it is set to OVERFLOW when the number is greater than 2^63-1, the
						// largest number representable by the type long.

	/**
	 * Reference to dummy node at the head.
	 */
	private Node head;

	/**
	 * Reference to dummy node at the tail.
	 */
	private Node tail;

	private int size; // number of distinct prime factors

	// ------------
	// Constructors
	// ------------

	/**
	 * Default constructor constructs an empty list to represent the number 1.
	 * 
	 * Combined with the add() method, it can be used to create a prime
	 * factorization.
	 */
	public PrimeFactorization() {
		head = new Node();
		tail = new Node();
		head.next = tail;
		tail.previous = head;
		value = 1;
		size = 0;
	}

	/**
	 * Obtains the prime factorization of n and creates a doubly linked list to
	 * store the result. Follows the direct search factorization algorithm in
	 * Section 1.2 of the project description.
	 * 
	 * @param n
	 * @throws IllegalArgumentException if n < 1
	 */
	public PrimeFactorization(long n) throws IllegalArgumentException {
		if (n < 1) {
			throw new IllegalArgumentException();
		}
		size = 0;
		long m = n;
		value = n;
		head = new Node();
		tail = new Node();
		head.next = tail;
		tail.previous = head;

		for (int d = 2; d * d <= m; d++) { // direct search factorization algorithm
			if (isPrime(d)) {
				if (m % d == 0) {
					m = m / d;
					int floor = d;
					int count = 1;
					while (m % d == 0) {
						m = m / d;
						count += 1;
					}
					link(tail.previous, new Node(new PrimeFactor(floor, count)));
					size++;
				}
			}
		}
		if (m > 1) { // adds a node if m is greater than 1
			link(tail.previous, new Node(new PrimeFactor((int) m, 1)));
			size++;
		}
	}

	/**
	 * Copy constructor. It is unnecessary to verify the primality of the numbers in
	 * the list.
	 * 
	 * @param pf
	 */
	public PrimeFactorization(PrimeFactorization pf) {
		head = new Node();
		tail = new Node();
		head.next = tail;
		tail.previous = head;
		size = pf.size;
		PrimeFactorizationIterator pfIter = pf.iterator();
		while (pfIter.hasNext()) {
			link(tail.previous, new Node(pfIter.next()));
		}
		updateValue();
	}

	/**
	 * Constructs a factorization from an array of prime factors. Useful when the
	 * number is too large to be represented even as a long integer.
	 * 
	 * @param pflist
	 */
	public PrimeFactorization(PrimeFactor[] pfList) {
		head = new Node();
		tail = new Node();
		head.next = tail;
		tail.previous = head;
		size = pfList.length;
		for (int i = 0; i < pfList.length; i++) { // copies pfList into the this.PrimeFactorization object
			link(tail.previous, new Node(pfList[i].prime, pfList[i].multiplicity));
		}
		updateValue();
	}

	// --------------
	// Primality Test
	// --------------

	/**
	 * Test if a number is a prime or not. Check iteratively from 2 to the largest
	 * integer not exceeding the square root of n to see if it divides n.
	 * 
	 * @param n
	 * @return true if n is a prime false otherwise
	 */
	public static boolean isPrime(long n) {
		if (n == 1 || n == 0) {
			return false;
		}
		for (int i = 2; i * i <= n; i++) { // checks if n is divisible by any number other than itself
			if (n % i == 0) {
				return false;
			}
		}
		return true;
	}

	// ---------------------------
	// Multiplication and Division
	// ---------------------------

	/**
	 * Multiplies the integer v represented by this object with another number n.
	 * Note that v may be too large (in which case this.value == OVERFLOW). You can
	 * do this in one loop: Factor n and traverse the doubly linked list
	 * simultaneously. For details refer to Section 3.1 in the project description.
	 * Store the prime factorization of the product. Update value and size.
	 * 
	 * @param n
	 * @throws IllegalArgumentException if n < 1
	 */
	public void multiply(long n) throws IllegalArgumentException {
		if (n < 1) {
			throw new IllegalArgumentException();
		}
		PrimeFactorizationIterator iter = this.iterator();
		long m = n;
		for (int d = 2; d * d <= m; d++) { // direct search factorization algorithm
			if (isPrime(d)) {
				if (m % d == 0) {
					m = m / d;
					int floor = d;
					int count = 1;
					while (m % d == 0) {
						m = m / d;
						count += 1;
					}
					this.add(floor, count);
					if (m > 1) { // adds primefactor(m,1) if m is more than 1
						int last = (int) m;
						this.add(last, 1);
					}
				}
			}
		}
		updateValue();
	}

	/**
	 * Multiplies the represented integer v with another number in the factorization
	 * form. Traverse both linked lists and store the result in this list object.
	 * See Section 3.1 in the project description for details of algorithm.
	 * 
	 * @param pf
	 */
	public void multiply(PrimeFactorization pf) {
		this.multiply(pf.value);
	}

	/**
	 * Multiplies the integers represented by two PrimeFactorization objects.
	 * 
	 * @param pf1
	 * @param pf2
	 * @return object of PrimeFactorization to represent the product
	 */
	public static PrimeFactorization multiply(PrimeFactorization pf1, PrimeFactorization pf2) {
		long product = pf1.value * pf2.value;
		return new PrimeFactorization(product);
	}

	/**
	 * Divides the represented integer v by n. Make updates to the list, value, size
	 * if divisible. No update otherwise. Refer to Section 3.2 in the project
	 * description for details.
	 * 
	 * @param n
	 * @return true if divisible false if not divisible
	 * @throws IllegalArgumentException if n <= 0
	 */
	public boolean dividedBy(long n) throws IllegalArgumentException {
		if (n <= 0) {
			throw new IllegalArgumentException();
		}
		if (value != -1 && value < n) {
			return false;
		}
		if (value == n) {
			clearList();
			return true;
		} else {
			PrimeFactorization pf = new PrimeFactorization(n); // PrimeFactorization object to be dividedBy
			if (dividedBy(pf) == true) {
				long count = value / n;
				PrimeFactorization newValue = new PrimeFactorization(count); // new PrimeFactorization object for count
				PrimeFactorizationIterator nIter = newValue.iterator();
				clearList(); // clears this PrimeFactorization for later additions
				updateValue();
				while (nIter.nextIndex() != newValue.size) { // copies new PrimeFactorization object to
																// this.PrimeFactorization
					this.add(nIter.cursor.pFactor.prime, nIter.cursor.pFactor.multiplicity);
					nIter.next();
				}
			}
			return true;
		}
	}

	/**
	 * Division where the divisor is represented in the factorization form. Update
	 * the linked list of this object accordingly by removing those nodes housing
	 * prime factors that disappear after the division. No update if this number is
	 * not divisible by pf. Algorithm details are given in Section 3.2.
	 * 
	 * @param pf
	 * @return true if divisible by pf false otherwise
	 */
	public boolean dividedBy(PrimeFactorization pf) {
		boolean result = false;
		if (value != -1 && pf.value != -1 && value < pf.value) {
			result = false;
		}
		if (value != -1 && pf.value == -1) {
			result = false;
		}
		if (value == pf.value) {
			clearList();
			result = false;
		} else {
			PrimeFactorizationIterator iter = iterator();
			PrimeFactorizationIterator iterPf = pf.iterator();
			while (iter.cursor.pFactor.prime < iterPf.cursor.pFactor.prime) { // if iters prime is less than iterPfs
																				// prime the number is not divisible and
																				// the iter moves to next until
																				// iter.hasNext() == false. If
																				// iter.hasNext() == false, returns
																				// false
				if (!iter.hasNext() && iterPf.hasNext()) {
					result = false;
				} else {
					iter.cursor = iter.cursor.next;
				}
			}
			if (iter.cursor.pFactor.prime > iterPf.cursor.pFactor.prime) { // if iters prime is greater than iterPfs
																			// prime the number is not divisible,
																			// returns false
				result = false;
			} else if (iter.cursor.pFactor.prime == iterPf.cursor.pFactor.prime
					&& iter.cursor.pFactor.multiplicity < iterPf.cursor.pFactor.multiplicity) { // if the primes are
																								// equal but iters
																								// multiplicity is less
																								// than iterPfs
																								// multiplicity, returns
																								// false
				result = false;
			} else if (iter.cursor.pFactor.prime == iterPf.cursor.pFactor.prime
					&& iter.cursor.pFactor.multiplicity >= iterPf.cursor.pFactor.multiplicity) { // if the primes are
																									// equal and iters
																									// multiplicity is
																									// >= iterPfs
																									// muliplicity,
																									// returns true
				result = true;
			}
		}

		return result;
	}

	/**
	 * Divide the integer represented by the object pf1 by that represented by the
	 * object pf2. Return a new object representing the quotient if divisible. Do
	 * not make changes to pf1 and pf2. No update if the first number is not
	 * divisible by the second one.
	 * 
	 * @param pf1
	 * @param pf2
	 * @return quotient as a new PrimeFactorization object if divisible null
	 *         otherwise
	 */
	public static PrimeFactorization dividedBy(PrimeFactorization pf1, PrimeFactorization pf2) {
		if (pf1.value % pf2.value != 0) {
			return null;
		} else {
			long quotient = pf1.value / pf2.value;
			return new PrimeFactorization(quotient);
		}
	}

	// -------------------------------------------------
	// Greatest Common Divisor and Least Common Multiple
	// -------------------------------------------------

	/**
	 * Computes the greatest common divisor (gcd) of the represented integer v and
	 * an input integer n. Returns the result as a PrimeFactor object. Calls the
	 * method Euclidean() if this.value != OVERFLOW.
	 * 
	 * It is more efficient to factorize the gcd than n, which can be much greater.
	 * 
	 * @param n
	 * @return prime factorization of gcd
	 * @throws IllegalArgumentException if n < 1
	 */
	public PrimeFactorization gcd(long n) throws IllegalArgumentException {
		if (n < 1) {
			throw new IllegalArgumentException();
		}
		if (value != OVERFLOW) {
			return new PrimeFactorization(Euclidean(value, n)); // uses Euclidean to find the gcd
		} else {
			return null;
		}
	}

	/**
	 * Implements the Euclidean algorithm to compute the gcd of two natural numbers
	 * m and n. The algorithm is described in Section 4.1 of the project
	 * description.
	 * 
	 * @param m
	 * @param n
	 * @return gcd of m and n.
	 * @throws IllegalArgumentException if m < 1 or n < 1
	 */
	public static long Euclidean(long m, long n) throws IllegalArgumentException {
		if (m < 1 || n < 1) {
			throw new IllegalArgumentException();
		}
		long remainder = m; // remainder of the division between m and n
		while (remainder != 0) {
			remainder = m % n;
			m = n;
			n = remainder;
		}
		return m;
	}

	/**
	 * Computes the gcd of the values represented by this object and pf by
	 * traversing the two lists. No direct computation involving value and pf.value.
	 * Refer to Section 4.2 in the project description on how to proceed.
	 * 
	 * @param pf
	 * @return prime factorization of the gcd
	 * @throws IllegalArgumentException if pf == null
	 */
	public PrimeFactorization gcd(PrimeFactorization pf) {
		if (pf == null) {
			throw new IllegalArgumentException();
		}
		return new PrimeFactorization(this.gcd(pf.value));
	}

	/**
	 * 
	 * @param pf1
	 * @param pf2
	 * @return prime factorization of the gcd of two numbers represented by pf1 and
	 *         pf2
	 * @throws IllegalArgumentException if pf1 == null or pf2 == null
	 */
	public static PrimeFactorization gcd(PrimeFactorization pf1, PrimeFactorization pf2) {
		if (pf1 == null || pf2 == null) {
			throw new IllegalArgumentException();
		}
		return new PrimeFactorization(pf1.gcd(pf2));

	}

	/**
	 * Computes the least common multiple (lcm) of the two integers represented by
	 * this object and pf. The list-based algorithm is given in Section 4.3 in the
	 * project description.
	 * 
	 * @param pf
	 * @return factored least common multiple
	 * @throws IllegalArgumentException if pf == null
	 */
	public PrimeFactorization lcm(PrimeFactorization pf) throws IllegalArgumentException {
		if (pf == null) {
			throw new IllegalArgumentException();
		}
		if (value == -1 || pf.value == -1) {
			pf.value = -1;
			return pf;
		}
		PrimeFactorization result = new PrimeFactorization(); // the PrimeFactorization object storing the lcm
		PrimeFactorizationIterator iter = iterator();
		PrimeFactorizationIterator pfIter = pf.iterator();
		while (iter.hasNext() && pfIter.hasNext()) { // traverses the lists
			if (iter.cursor.pFactor.prime != pfIter.cursor.pFactor.prime) {
				if (iter.cursor.pFactor.prime < pfIter.cursor.pFactor.prime) { // adds iters prime if it is lower than
																				// pfIters prime
					PrimeFactor add = new PrimeFactor(iter.cursor.pFactor.prime, iter.cursor.pFactor.multiplicity);
					result.add(add.prime, add.multiplicity);
					iter.next();
				} else { // adds pfIters prime because it is lower than iters prime
					PrimeFactor add = new PrimeFactor(pfIter.cursor.pFactor.prime, pfIter.cursor.pFactor.multiplicity);
					result.add(add.prime, add.multiplicity);
					pfIter.next();
				}
			} else { // adds the lower multiplicity if they are equal
				PrimeFactor add = new PrimeFactor(iter.cursor.pFactor.prime,
						Math.max(iter.cursor.pFactor.multiplicity, pfIter.cursor.pFactor.multiplicity));
				result.add(add.prime, add.multiplicity);
				iter.next();
				pfIter.next();
			}
		}
		if (iter.hasNext()) { // deals with remaining prime factor if there are any
			while (iter.hasNext()) {
				PrimeFactor add = new PrimeFactor(iter.cursor.pFactor.prime, iter.cursor.pFactor.multiplicity);
				result.add(add.prime, add.multiplicity);
				iter.next();
			}
		}
		if (pfIter.hasNext()) { // deals with remaining prime factor if there are any
			while (pfIter.hasNext()) {
				PrimeFactor add = new PrimeFactor(pfIter.cursor.pFactor.prime, pfIter.cursor.pFactor.multiplicity);
				result.add(add.prime, add.multiplicity);
				pfIter.next();
			}
		}
		result.updateValue();
		return result;
	}

	/**
	 * Computes the least common multiple of the represented integer v and an
	 * integer n. Construct a PrimeFactors object using n and then call the lcm()
	 * method above. Calls the first lcm() method.
	 * 
	 * @param n
	 * @return factored least common multiple
	 * @throws IllegalArgumentException if n < 1
	 */
	public PrimeFactorization lcm(long n) throws IllegalArgumentException {
		if (n < 1) {
			throw new IllegalArgumentException();
		}
		return new PrimeFactorization(lcm(new PrimeFactorization(n)));
	}

	/**
	 * Computes the least common multiple of the integers represented by pf1 and
	 * pf2.
	 * 
	 * @param pf1
	 * @param pf2
	 * @return prime factorization of the lcm of two numbers represented by pf1 and
	 *         pf2
	 * @throws IllegalArgumentException if pf1 == null or pf2 == null
	 */
	public static PrimeFactorization lcm(PrimeFactorization pf1, PrimeFactorization pf2)
			throws IllegalArgumentException {
		if (pf1 == null || pf2 == null) {
			throw new IllegalArgumentException();
		}
		return new PrimeFactorization(pf1.lcm(pf2));
	}

	// ------------
	// List Methods
	// ------------

	/**
	 * Traverses the list to determine if p is a prime factor.
	 * 
	 * Precondition: p is a prime.
	 * 
	 * @param p
	 * @return true if p is a prime factor of the number v represented by this
	 *         linked list false otherwise
	 */
	public boolean containsPrimeFactor(int p) {
		boolean pOfV = false;
		PrimeFactorizationIterator iter = iterator();
		while (iter.hasNext()) {
			if (iter.next().prime == p) {
				pOfV = true;
			}
		}
		return pOfV;
	}

	// The next two methods ought to be private but are made public for testing
	// purpose. Keep
	// them public

	/**
	 * Adds a prime factor p of multiplicity m. Search for p in the linked list. If
	 * p is found at a node N, add m to N.multiplicity. Otherwise, create a new node
	 * to store p and m.
	 * 
	 * Precondition: p is a prime.
	 * 
	 * @param p prime
	 * @param m multiplicity
	 * @return true if m >= 1 false if m < 1
	 */
	public boolean add(int p, int m) {
		boolean found = false;
		if (m < 1) {
			return false;
		} else {
			PrimeFactorizationIterator iter = iterator();
			while (iter.hasNext()) { // if primes are equal increments multiplicity
				if (iter.cursor.pFactor.prime == p) {
					iter.cursor.pFactor.multiplicity += m;
					updateValue();
					found = true;
				}
				iter.next();
			}
			iter = iterator(); // resets iterator
			if (found != true) {
				while (iter.hasNext() && iter.cursor.pFactor.prime < p) { // traverses iter until iters prime >= p
					iter.next();
				}
				if (iter.cursor == head.next) { // for when adding to empty PrimeFactorization object
					if (iter.cursor.pFactor == null) {
						iter.cursor.next = tail.previous;
						link(iter.cursor.previous, new Node(p, m));
						size++;
						updateValue();
						found = true;
					}
				}
				if (found != true) { // for adding to the front of PrimeFactorization object
					iter.previous();
					if (iter.cursor.previous.pFactor == null) {
						iter.cursor.previous = head;
						link(iter.cursor, new Node(p, m));
						size++;
						updateValue();
						found = true;
					} else { // any other addition placement
						link(iter.cursor, new Node(p, m));
						size++;
						updateValue();
						found = true;
					}
				}
			}
			return found;
		}
	}

	/**
	 * Removes m from the multiplicity of a prime p on the linked list. It starts by
	 * searching for p. Returns false if p is not found, and true if p is found. In
	 * the latter case, let N be the node that stores p. If N.multiplicity > m,
	 * subtracts m from N.multiplicity. If N.multiplicity <= m, removes the node N.
	 * 
	 * Precondition: p is a prime.
	 * 
	 * @param p
	 * @param m
	 * @return true when p is found. false when p is not found.
	 * @throws IllegalArgumentException if m < 1
	 */
	public boolean remove(int p, int m) throws IllegalArgumentException {
		if (m < 1) {
			throw new IllegalArgumentException();
		}
		boolean found = false;
		PrimeFactorizationIterator iter = this.iterator();
		while (iter.hasNext()) {
			if (iter.cursor.pFactor.prime == p && iter.cursor.pFactor.multiplicity == m) { // N.multiplicity = m,
																							// removes the node N
				iter.next();
				iter.remove();
				found = true;
			} else if (iter.cursor.pFactor.prime == p && iter.cursor.pFactor.multiplicity < m) { // N.multiplicity < m,
																									// removes the node
																									// N
				iter.next();
				iter.remove();
				found = true;
			} else if (iter.cursor.pFactor.prime == p && iter.cursor.pFactor.multiplicity > m) { // N.multiplicity > m,
																									// subtracts m from
																									// N.multiplicity
				iter.cursor.pFactor.multiplicity -= m;
				updateValue();
				found = true;
			} else {
				found = false;
			}
			if (iter.hasNext()) {
				iter.next();
			}
		}
		return found;
	}

	/**
	 * 
	 * @return size of the list
	 */
	public int size() {
		return size;
	}

	/**
	 * Writes out the list as a factorization in the form of a product. Represents
	 * exponentiation by a caret. For example, if the number is 5814, the returned
	 * string would be printed out as "2 * 3^2 * 17 * 19".
	 */
	@Override
	public String toString() {
		String result = "";
		PrimeFactorizationIterator iter = iterator();
		if (value == 1) {
			result += 1;
			return result;
		}
		while (iter.hasNext()) {
			result += iter.next().toString();
			if (iter.hasNext()) {
				result += " * ";
			}
		}
		return result;
	}

	// The next three methods are for testing, but you may use them as you like.

	/**
	 * @return true if this PrimeFactorization is representing a value that is too
	 *         large to be within long's range. e.g. 999^999. false otherwise.
	 */
	public boolean valueOverflow() {
		return value == OVERFLOW;
	}

	/**
	 * @return value represented by this PrimeFactorization, or -1 if
	 *         valueOverflow()
	 */
	public long value() {
		return value;
	}

	public PrimeFactor[] toArray() {
		PrimeFactor[] result = new PrimeFactor[size];
		PrimeFactorizationIterator iter = iterator();
		int i = 0;
		while (iter.hasNext()) {
			result[i] = iter.next();
			i++;
		}
		return result;
	}

	@Override
	public PrimeFactorizationIterator iterator() {
		return new PrimeFactorizationIterator();
	}

	/**
	 * Doubly-linked node type for this class.
	 */
	private class Node {
		public PrimeFactor pFactor; // prime factor
		public Node next;
		public Node previous;

		/**
		 * Default constructor for creating a dummy node.
		 */
		public Node() {
		}

		/**
		 * Precondition: p is a prime
		 * 
		 * @param p prime number
		 * @param m multiplicity
		 * @throws IllegalArgumentException if m < 1
		 */
		public Node(int p, int m) throws IllegalArgumentException {
			if (m < 1) {
				throw new IllegalArgumentException();
			}
			pFactor = new PrimeFactor(p, m);
		}

		/**
		 * Constructs a node over a provided PrimeFactor object.
		 * 
		 * @param pf
		 * @throws IllegalArgumentException
		 */
		public Node(PrimeFactor pf) {
			pFactor = new PrimeFactor(pf.prime, pf.multiplicity);
		}

		/**
		 * Printed out in the form: prime + "^" + multiplicity. For instance "2^3".
		 * Also, deal with the case pFactor == null in which a string "dummy" is
		 * returned instead.
		 */
		@Override
		public String toString() {
			return pFactor.toString();
		}
	}

	private class PrimeFactorizationIterator implements ListIterator<PrimeFactor> {
		// Class invariants:
		// 1) logical cursor position is always between cursor.previous and cursor
		// 2) after a call to next(), cursor.previous refers to the node just returned
		// 3) after a call to previous() cursor refers to the node just returned
		// 4) index is always the logical index of node pointed to by cursor

		private Node cursor = head.next;
		private Node pending = null; // node pending for removal
		private int index = 0;

		// other instance variables ...

		/**
		 * Default constructor positions the cursor before the smallest prime factor.
		 */
		public PrimeFactorizationIterator() {
			cursor = head.next;
		}

		@Override
		public boolean hasNext() {
			return cursor.next != tail.next;
		}

		@Override
		public boolean hasPrevious() {
			return cursor.previous != head.previous;
		}

		@Override
		public PrimeFactor next() {
			if (index != size) {
				index++;
				pending = cursor;
				cursor = cursor.next;
				cursor.previous = pending;
			}
			return pending.pFactor;
		}

		@Override
		public PrimeFactor previous() {
			if (index != 0) {
				index--;
				Node temp = cursor;
				pending = cursor.previous;
				cursor = cursor.previous;
				cursor.next = temp;
				cursor.previous = temp.previous.previous;
			}
			return pending.pFactor;
		}

		/**
		 * Removes the prime factor returned by next() or previous()
		 * 
		 * @throws IllegalStateException if pending == null
		 */
		@Override
		public void remove() throws IllegalStateException {
			if (pending == null) {
				throw new IllegalStateException();
			}
			if (pending == cursor) {
				cursor = cursor.next;
				unlink(pending);
				pending = null;
				updateValue();
				size--;
			} else {
				unlink(pending);
				pending = null;
				updateValue();
				index--;
				size--;
			}
			return;
		}

		/**
		 * Adds a prime factor at the cursor position. The cursor is at a wrong position
		 * in either of the two situations below:
		 * 
		 * a) pf.prime < cursor.previous.pFactor.prime if cursor.previous != head. b)
		 * pf.prime > cursor.pFactor.prime if cursor != tail.
		 * 
		 * Take into account the possibility that pf.prime == cursor.pFactor.prime.
		 * 
		 * Precondition: pf.prime is a prime.
		 * 
		 * @param pf
		 * @throws IllegalArgumentException if the cursor is at a wrong position.
		 */
		@Override
		public void add(PrimeFactor pf) throws IllegalArgumentException {
			if (cursor.previous != head && pf.prime < cursor.previous.pFactor.prime) {
				throw new IllegalArgumentException();
			}
			if (cursor != tail && pf.prime > cursor.pFactor.prime) {
				throw new IllegalArgumentException();
			}
			if (pf.prime == cursor.pFactor.prime) {
				cursor.pFactor.multiplicity += pf.multiplicity;
			} else {
				link(cursor.previous, new Node(pf));
				updateValue();
				index++;
				size++;
			}
		}

		@Override
		public int nextIndex() {
			return index;
		}

		@Override
		public int previousIndex() {
			return index - 1;
		}

		@Deprecated
		@Override
		public void set(PrimeFactor pf) {
			throw new UnsupportedOperationException(getClass().getSimpleName() + " does not support set method");
		}

		// Other methods you may want to add or override that could possibly facilitate
		// other operations, for instance, addition, access to the previous element,
		// etc.
		//
		// ...
		//
	}

	// --------------
	// Helper methods
	// --------------

	/**
	 * Inserts toAdd into the list after current without updating size.
	 * 
	 * Precondition: current != null, toAdd != null
	 */
	private void link(Node current, Node toAdd) {
		toAdd.previous = current;
		toAdd.next = current.next;
		current.next.previous = toAdd;
		current.next = current.next.previous;
	}

	/**
	 * Removes toRemove from the list without updating size.
	 */
	private void unlink(Node toRemove) {
		toRemove.next.previous = toRemove.previous;
		toRemove.previous.next = toRemove.next;
	}

	/**
	 * Remove all the nodes in the linked list except the two dummy nodes.
	 * 
	 * Made public for testing purpose. Ought to be private otherwise.
	 */
	public void clearList() {
		head.next = tail;
		tail.previous = head;
		size = 0;
		value = 1;
	}

	/**
	 * Multiply the prime factors (with multiplicities) out to obtain the
	 * represented integer. Use Math.multiply(). If an exception is throw, assign
	 * OVERFLOW to the instance variable value. Otherwise, assign the multiplication
	 * result to the variable.
	 * 
	 */
	private void updateValue() {
		try {
			PrimeFactorizationIterator iter = iterator();
			long result = 1;
			while (iter.hasNext()) {
				PrimeFactor p = iter.next();
				result = Math.multiplyExact(result, p.prime);
			}
			value = result;
		}

		catch (ArithmeticException e) {
			value = OVERFLOW;
		}

	}
}
