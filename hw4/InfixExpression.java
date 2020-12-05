package edu.iastate.cs228.hw4;

/**
 *  
 * @author Brad Warren: bawarren@iastate.edu
 *
 */

import java.util.HashMap;
import java.util.Scanner;
import edu.iastate.cs228.hw4.Operator;

/**
 * 
 * This class represents an infix expression. It implements infix to postfix
 * conversion using one stack, and evaluates the converted postfix expression.
 *
 */

public class InfixExpression extends Expression {
	private String infixExpression; // the infix expression to convert
	private boolean postfixReady = false; // postfix already generated if true
	private int rankTotal = 0; // Keeps track of the cumulative rank of the infix expression.

	private PureStack<Operator> operatorStack; // stack of operators

	/**
	 * Constructor stores the input infix string, and initializes the operand stack
	 * and the hash map.
	 * 
	 * @param st     input infix string.
	 * @param varTbl hash map storing all variables in the infix expression and
	 *               their values.
	 */
	public InfixExpression(String st, HashMap<Character, Integer> varTbl) {
		super(st, varTbl);
		infixExpression = st;
		operatorStack = new ArrayBasedStack<Operator>();
	}

	/**
	 * Constructor supplies a default hash map.
	 * 
	 * @param s
	 */
	public InfixExpression(String s) {
		infixExpression = s;
		operatorStack = new ArrayBasedStack<Operator>();
	}

	/**
	 * Outputs the infix expression according to the format in the project
	 * description.
	 */
	@Override
	public String toString() {
		Scanner scan = new Scanner(infixExpression);
		String result = " ";
		while (scan.hasNext()) {
			result += scan.next() + " ";
		}
		infixExpression = result.trim();
		scan.close();
		return result.trim();
	}

	/**
	 * @return equivalent postfix expression, or
	 * 
	 *         a null string if a call to postfix() inside the body (when
	 *         postfixReady == false) throws an exception.
	 * @throws Exception
	 */
	public String postfixString() throws Exception {
		if (postfixReady == false) {
			throw new Exception("Not postfix ready");
		}
		String result = " ";
		for (int i = 0; i < postfixExpression.length(); i++) {
			result += postfixExpression.charAt(i);
		}
		return result.trim();
	}

	/**
	 * Resets the infix expression.
	 * 
	 * @param st
	 */
	public void resetInfix(String st) {
		infixExpression = toString();
	}

	/**
	 * Converts infix expression to an equivalent postfix string stored at
	 * postfixExpression. If postfixReady == false, the method scans the
	 * infixExpression, and does the following (for algorithm details refer to the
	 * relevant PowerPoint slides):
	 * 
	 * 1. Skips a whitespace character. 2. Writes a scanned operand to
	 * postfixExpression. 3. When an operator is scanned, generates an operator
	 * object. In case the operator is determined to be a unary minus, store the
	 * char '~' in the generated operator object. 4. If the scanned operator has a
	 * higher input precedence than the stack precedence of the top operator on the
	 * operatorStack, push it onto the stack. 5. Otherwise, first calls
	 * outputHigherOrEqual() before pushing the scanned operator onto the stack. No
	 * push if the scanned operator is ). 6. Keeps track of the cumulative rank of
	 * the infix expression.
	 * 
	 * During the conversion, catches errors in the infixExpression by throwing
	 * ExpressionFormatException with one of the following messages:
	 * 
	 * -- "Operator expected" if the cumulative rank goes above 1; -- "Operand
	 * expected" if the rank goes below 0; -- "Missing '('" if scanning a ‘)’
	 * results in popping the stack empty with no '('; -- "Missing ')'" if a '(' is
	 * left unmatched on the stack at the end of the scan; -- "Invalid character" if
	 * a scanned char is neither a digit nor an operator;
	 * 
	 * If an error is not one of the above types, throw the exception with a message
	 * you define.
	 * 
	 * Sets postfixReady to true.
	 */
	public void postfix() throws ExpressionFormatException, UnassignedVariableException {
		int index = 0;
		Scanner scan = new Scanner(infixExpression);
		while (postfixReady == false) {
			postfixExpression = " ";
			while (scan.hasNext()) {
				String current = scan.next();
				if (isInt(current) == false && isVariable(current.charAt(0)) == false
						&& isOperator(current.charAt(0)) == false && current != " ") { // Checks if the the current
																						// character is a valid
																						// character
					scan.close();
					throw new ExpressionFormatException("Invalid character");
				} else if (current == " ") {

				} else if (isInt(current) == true) { // If cur is an integer we increment the rankTotal and add it to
														// the
														// postfixExpression
					rankTotal += 1;
					postfixExpression += current + " ";
				} else if (isVariable(current.charAt(0)) == true) { // If current.charAt(0) is a variable
																	// we increment rank and add it to the
																	// postfixExpression
					rankTotal += 1;
					postfixExpression += current.charAt(0) + " ";
				} else if (isOperator(current.charAt(0)) == true) { // If current.charAt(0) is an operator
																	// we check several cases
					Operator op = new Operator(current.charAt(0));
					if (op.getOp() == '-' && isUnary(index) == true) { // Changes op to unary if isUnary() is true
						op = new Operator('~');
					} else if (op.getOp() != '(' && op.getOp() != ')') { // Decrements rank by operators that aren't '('
																			// or ')'
						rankTotal -= 1;
					}
					if (!operatorStack.isEmpty()) {
						if (operatorStack.peek().compareTo(op) < 0) { // Checks if inputPrecedence of op is higher than
																		// the
																		// top of operatorStack's stackPrecedence
							operatorStack.push(op);
						} else {
							outputHigherOrEqual(op); // Pops operatorStack according to outputHigherOrEqual method
							if (op.operator != ')') {
								operatorStack.push(op);
							}
						}
					} else {
						operatorStack.push(op); // Pushes operator onto the stack
					}
				}
				if (rankTotal > 1) {
					scan.close();
					throw new ExpressionFormatException("Operator expected");
				} else if (rankTotal < 0) {
					scan.close();
					throw new ExpressionFormatException("Operand expected");
				}
				if (!operatorStack.isEmpty()) {
					if (current.charAt(0) == ')') {
						while (!operatorStack.isEmpty()) { // Pops operatorStack onto postfixExpression until
															// corresponding parentheses is
															// found
							if (operatorStack.peek().operator != '(') {
								postfixExpression += operatorStack.pop().operator + " ";
							} else {
								operatorStack.pop();
							}
						}
					}
				}
				if (!operatorStack.isEmpty()) {
					if (operatorStack.peek().operator == '(' && scan.hasNext() == false && current.charAt(0) != ')') {
						scan.close();
						throw new ExpressionFormatException("Missing ')'");
					} else if (scan.hasNext() == false) {
						while (!operatorStack.isEmpty()) { // Pops remaining operators into postfixExpression
							postfixExpression += operatorStack.pop().operator + " ";
						}
					}
				}
				if (operatorStack.isEmpty() && scan.hasNext() == false) { // Determines if full scan is completed
					postfixExpression = postfixExpression.trim();
					postfixReady = true;
				}
				index++;
			}
		}
		scan.close();
	}

	/**
	 * This function first calls postfix() to convert infixExpression into
	 * postfixExpression. Then it creates a PostfixExpression object and calls its
	 * evaluate() method (which may throw an exception). It also passes any
	 * exception thrown by the evaluate() method of the PostfixExpression object
	 * upward the chain.
	 * 
	 * @return value of the infix expression
	 * @throws ExpressionFormatException, UnassignedVariableException
	 */
	public int evaluate() throws ExpressionFormatException, UnassignedVariableException {
		postfix();
		PostfixExpression result = new PostfixExpression(postfixExpression, varTable);
		return result.evaluate();
	}

	/**
	 * Pops the operator stack and output as long as the operator on the top of the
	 * stack has a stack precedence greater than or equal to the input precedence of
	 * the current operator op. Writes the popped operators to the string
	 * postfixExpression.
	 * 
	 * If op is a ')', and the top of the stack is a '(', also pops '(' from the
	 * stack but does not write it to postfixExpression.
	 * 
	 * @param op current operator
	 * @throws ExpressionFormatException with the following message -- "Missing '('"
	 *                                   if op is a ')' and matching '(' is not
	 *                                   found on stack.
	 */
	private void outputHigherOrEqual(Operator op) throws ExpressionFormatException {
		if (op.operator == ')' && operatorStack.isEmpty()) {
			throw new ExpressionFormatException("Missing ')'");
		}
		if (op.operator == ')' && operatorStack.peek().operator == '(') {
			operatorStack.pop();
		} else {
			if (operatorStack.peek().compareTo(op) >= 0) {
				postfixExpression += operatorStack.pop().operator + " ";
			}
		}
	}
	// other helper methods if needed

	/**
	 * Checks the 3 conditions that would make a '-' character unary. 1. first in an
	 * expression, as in - 3 * 5 - 7 (where the first ‘-‘ is a unary minus operator
	 * and the second ‘-‘ is a binary minus operator); 2. after another operator
	 * (either binary or unary), as in 8 - - - 10 * - 2 (where the first ‘-‘ is
	 * binary, and the next three ‘-‘s are unary); 3. after a left parenthesis
	 * (i.e., first in a subexpression), as in x / (- y + 2).
	 * 
	 * @param index
	 * @return true if unary, false otherwise
	 */
	private boolean isUnary(int index) {
		boolean unary = false;
		Scanner scan = new Scanner(infixExpression);
		String cur = scan.next();
		int count = 0;
		String last = " "; // Previous character
		if (count != index) { // Continues until we reach the proper index
			while (scan.hasNext()) {
				if (cur.equals(" ")) {
					cur = scan.next();
					count++;
				} else {
					last = cur;
					cur = scan.next();
					count++;
				}
				if (count == index) {
					break;
				}
			}
		}
		// The three cases that decide if "-" is unary
		if (count == 0) {
			unary = true;
		} else if (count > 1 && cur.equals("-") && isOperator(last.charAt(0)) == true && !last.equals(")")) {
			unary = true;
		} else if (count > 1 && cur.equals("-") && last.equals("(")) {
			unary = true;
		}
		scan.close();
		return unary;

	}
}
