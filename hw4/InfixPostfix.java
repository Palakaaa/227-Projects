package edu.iastate.cs228.hw4;

import java.io.File;

/**
 *  
 * @author Brad Warren: bawarren@iastate.edu
 *
 */

/**
 * 
 * This class evaluates input infix and postfix expressions. 
 *
 */

import java.util.HashMap;
import java.util.Scanner;

public class InfixPostfix {

	public static int numbVar = 0;
	public static String vars = "";

	/**
	 * Repeatedly evaluates input infix and postfix expressions. See the project
	 * description for the input description. It constructs a HashMap object for
	 * each expression and passes it to the created InfixExpression or
	 * PostfixExpression object.
	 * 
	 * @param args
	 * @throws Exception
	 **/
	public static void main(String[] args) throws Exception {
		System.out.println("Evaluation of Infix and Postfix Expressions");
		System.out.println("keys: 1 (standard input) 2 (file input) 3 (exit)");
		System.out.println("(Enter “I” before an infix expression, “P” before a postfix expression”)");
		System.out.println("");
		System.out.println("Trial 1: ");
		int trials = 1;
		Scanner scan = new Scanner(System.in);
		int input = scan.nextInt();
		while (input == 1 || input == 2) {
			vars = "";
			numbVar = 0;
			if (input == 1) { // Expression comes from standard input
				System.out.println("Expression: ");
				String expression = scan.next();

				if (expression.charAt(0) == 'I') { // If infix expression
					expression = scan.nextLine();
					if (hasVar(expression)) { // Finds how many variables there are/what the variables are

						HashMap<Character, Integer> x = new HashMap<Character, Integer>();
						InfixExpression ex = new InfixExpression(expression, x);
						System.out.println("Infix form: " + ex.toString());
						ex.postfix(); // Constructs the postfix expression
						System.out.println("Postfix form: " + ex.postfixExpression);
						System.out.println("where");
						int count = 1;
						while (count <= numbVar) {
							System.out.println(vars.charAt(count - 1) + " = ");
							ex.varTable.put(vars.charAt(count - 1), scan.nextInt()); // Stores the variable and its
																						// value into varTable
							count++;
						}
						System.out.println("Expression value: " + ex.evaluate()); // Evaluates the expression
					} else {
						InfixExpression ex = new InfixExpression(expression);
						System.out.println("Infix form:" + ex.toString());
						ex.postfix(); // Constructs the postfix expression
						System.out.println("Postfix form: " + ex.postfixExpression);
						System.out.println("Expression value: " + ex.evaluate()); // Evaluates the expression
					}
				} else if (expression.charAt(0) == 'P') {
					expression = scan.nextLine();

					if (hasVar(expression)) { // Finds how many variables there are/what the variables are
						HashMap<Character, Integer> x = new HashMap<Character, Integer>();
						PostfixExpression ex = new PostfixExpression(expression, x);
						System.out.println("Postfix form: " + ex.toString());
						System.out.println("where");
						int count = 1;
						while (count <= numbVar) {
							System.out.println(vars.charAt(count - 1) + " = ");
							ex.varTable.put(vars.charAt(count - 1), scan.nextInt()); // Stores the variable and its
																						// value into varTable
							count++;
						}
						System.out.println("Expression value: " + ex.evaluate()); // Evaluates the expression
					} else {
						PostfixExpression ex = new PostfixExpression(expression);
						System.out.println("Postfix form: " + ex.toString());
						System.out.println("Expression value: " + ex.evaluate()); // Evaluates the expression
					}
				}
			} else if (input == 2) { // Expression comes from a file
				System.out.println("Input from a file");
				System.out.println("Enter file name: ");
				Scanner sc = new Scanner(new File(scan.next()));
				System.out.println(" ");

				while (sc.hasNextLine()) {
					numbVar = 0;
					vars = "";
					String expression = sc.next();

					if (expression.charAt(0) == 'I') { // If infix expression
						expression = sc.nextLine();
						if (hasVar(expression)) { // Finds how many variables there are/what the variables are
							HashMap<Character, Integer> x = new HashMap<Character, Integer>();
							InfixExpression ex = new InfixExpression(expression, x);
							System.out.println("Infix form: " + ex.toString());
							ex.postfix(); // Constructs the postfix expression
							System.out.println("Postfix form: " + ex.postfixExpression);
							System.out.println("where");
							int count = 1;
							while (count <= numbVar) {
								String var = sc.nextLine();
								int tracker = 0; // Acts as a index count
								int v = 0; // Holds temporary integers
								String va = ""; // Stores the string result of the value of the variable
								while (tracker != var.length()) {
									String spot = Character.toString(var.charAt(tracker)); // String location of tested
																							// character
									try {
										v = Integer.parseInt(spot);
									} catch (NumberFormatException e) {

									}
									va += v;
									tracker++;
								}
								int val = Integer.parseInt(va); // Integer value of the integer
								System.out.println(vars.charAt(count - 1) + " = " + val);
								ex.varTable.put(vars.charAt(count - 1), val); // Stores the variable and its value into
																				// varTable
								count++;
							}
							System.out.println("Expression value: " + ex.evaluate()); // Evaluates the expression
						} else {
							InfixExpression ex = new InfixExpression(expression);
							System.out.println("Infix form: " + ex.toString());
							ex.postfix(); // Constructs the postfix expression
							System.out.println("Postfix form: " + ex.postfixExpression);
							System.out.println("Expression value: " + ex.evaluate()); // Evaluates the expression
						}
					} else if (expression.charAt(0) == 'P') {
						expression = sc.nextLine();
						if (hasVar(expression)) { // Finds how many variables there are/what the variables are
							HashMap<Character, Integer> x = new HashMap<Character, Integer>();
							PostfixExpression ex = new PostfixExpression(expression, x);
							System.out.println("Postfix form: " + ex.toString());
							System.out.println("where");
							int count = 1;
							while (count <= numbVar) {
								String var = sc.nextLine();
								int tracker = 0; // Acts as a index count
								int v = 0; // Holds temporary integers
								String va = ""; // Stores the string result of the value of the variable
								while (tracker != var.length()) {
									String spot = Character.toString(var.charAt(tracker)); // String location of tested
																							// character
									try {
										v = Integer.parseInt(spot);
									} catch (NumberFormatException e) {

									}
									va += v;
									tracker++;
								}
								int val = Integer.parseInt(va); // Integer value of the integer
								System.out.println(vars.charAt(count - 1) + " = " + val);
								ex.varTable.put(vars.charAt(count - 1), val); // Stores the variable and its value into
																				// varTable
								count++;
							}
							System.out.println("Expression value: " + ex.evaluate()); // Evaluates the expression

						} else {
							PostfixExpression ex = new PostfixExpression(expression);
							System.out.println("Postfix form: " + ex.toString());
							System.out.println("Expression value: " + ex.evaluate()); // Evaluates the expression
						}
					}
				}
			} else if (input == 3) {
				scan.close();
				break;
			}
			trials += 1;
			System.out.println("Trial " + trials + ":");
			input = scan.nextInt();
		}
	}

	// helper methods if needed

	/**
	 * Helper method, used in hasVar(String s) to check if the character of a string
	 * is a variable.
	 * 
	 * @param c
	 * @return true if c is a variable, false otherwise
	 */
	public static boolean isVar(char c) {
		String letters = "abcdefghijklmnopqrstuvwxyz";
		for (int i = 0; i < letters.length(); i++) {
			if (c == letters.charAt(i)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Helper method, used to check is a string s contains variables. Updates vars
	 * to contain variables within string s, and numbVar to equal the number of
	 * different variables.
	 * 
	 * @param s
	 * @return returns true if the string contains variables, false otherwise
	 */
	public static boolean hasVar(String s) {
		boolean var = false;
		char f = ' ';
		for (int i = 0; i < s.length(); i++) {
			if (isVar(s.charAt(i)) && s.charAt(i) != f) {
				f = s.charAt(i);
				vars += s.charAt(i); // Updates vars
				numbVar += 1; // Updates numbVar
				var = true;
			}
		}
		return var;
	}
}
