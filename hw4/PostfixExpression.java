package edu.iastate.cs228.hw4;

/**
 *  
 * @author Brad Warren: bawarren@iastate.edu
 *
 */

/**
 * 
 * This class evaluates a postfix expression using one stack.    
 *
 */

import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class PostfixExpression extends Expression {
	private int leftOperand; // left operand for the current evaluation step
	private int rightOperand; // right operand (or the only operand in the case of
								// a unary minus) for the current evaluation step

	private PureStack<Integer> operandStack; // stack of operands

	/**
	 * Constructor stores the input postfix string and initializes the operand
	 * stack.
	 * 
	 * @param st     input postfix string.
	 * @param varTbl hash map that stores variables from the postfix string and
	 *               their values.
	 */
	public PostfixExpression(String st, HashMap<Character, Integer> varTbl) {
		super(st, varTbl);
		postfixExpression = st;
		operandStack = new ArrayBasedStack<Integer>();
	}

	/**
	 * Constructor supplies a default hash map.
	 * 
	 * @param s
	 */
	public PostfixExpression(String s) {
		postfixExpression = s;
		operandStack = new ArrayBasedStack<Integer>();
		varTable = new HashMap<Character, Integer>();
	}

	/**
	 * Outputs the postfix expression according to the format in the project
	 * description.
	 */
	@Override
	public String toString() {
		Scanner scan = new Scanner(postfixExpression);
		String result = " ";
		while (scan.hasNext()) {
			result += scan.next() + " ";
		}
		postfixExpression = result.trim();
		scan.close();
		return result.trim();
	}

	/**
	 * Resets the postfix expression.
	 * 
	 * @param st
	 */
	public void resetPostfix(String st) {
		postfixExpression = toString();
	}

	/**
	 * Scan the postfixExpression and carry out the following:
	 * 
	 * 1. Whenever an integer is encountered, push it onto operandStack. 2. Whenever
	 * a binary (unary) operator is encountered, invoke it on the two (one) elements
	 * popped from operandStack, and push the result back onto the stack. 3. On
	 * encountering a character that is not a digit, an operator, or a blank space,
	 * stop the evaluation.
	 * 
	 * @return value of the postfix expression
	 * @throws ExpressionFormatException   with one of the messages below:
	 * 
	 *                                     -- "Invalid character" if encountering a
	 *                                     character that is not a digit, an
	 *                                     operator or a whitespace (blank, tab); --
	 *                                     "Too many operands" if operandStack is
	 *                                     non-empty at the end of evaluation; --
	 *                                     "Too many operators" if getOperands()
	 *                                     throws NoSuchElementException; -- "Divide
	 *                                     by zero" if division or modulo is the
	 *                                     current operation and rightOperand == 0;
	 *                                     -- "0^0" if the current operation is "^"
	 *                                     and leftOperand == 0 and rightOperand ==
	 *                                     0; -- self-defined message if the error
	 *                                     is not one of the above.
	 * 
	 *                                     UnassignedVariableException if the
	 *                                     operand as a variable does not have a
	 *                                     value stored in the hash map. In this
	 *                                     case, the exception is thrown with the
	 *                                     message
	 * 
	 *                                     -- "Variable <name> was not assigned a
	 *                                     value", where <name> is the name of the
	 *                                     variable.
	 * @throws UnassignedVariableException
	 * 
	 */
	public int evaluate() throws ExpressionFormatException, UnassignedVariableException {
		Scanner scan = new Scanner(postfixExpression);
		while (scan.hasNext()) {
			String current = scan.next();
			if (isInt(current) == false && isOperator(current.charAt(0)) == false
					&& isVariable(current.charAt(0)) == false && current != " ") {// Checks if the
																					// the current character is a valid
																					// character
				scan.close();
				throw new ExpressionFormatException("Invalid Character");

			} else if (current == " ") {

			} else if (isInt(current) == true) { // If current is an integer we push it onto the operandStack
				int op = Integer.parseInt(current);
				operandStack.push(op);

			} else if (isVariable(current.charAt(0))) { // If current.charAt(0) is a variable, we check
														// if varTable has it as a variable. If so, we push its
														// value onto the operandStack
				if (varTable.containsKey(current.charAt(0))) {
					operandStack.push(varTable.get(current.charAt(0)));
				} else {
					scan.close();
					throw new UnassignedVariableException("Variable" + current.charAt(0) + "was not assigned a value");
				}

			} else if (isOperator(current.charAt(0)) == true) { // If current.charAt(0) is an operator,
																// we try run it through cases that would throw
																// an exception. If it passes all, then we push
																// the computation of it and the top to operands
																// of the stack
				try {
					getOperands(current.charAt(0));
				} catch (NoSuchElementException e) {
					scan.close();
					throw new NoSuchElementException("Too many operators");
				}
				if ((current.charAt(0) == '/' || current.charAt(0) == '%') && rightOperand == 0) {
					scan.close();
					throw new ExpressionFormatException("Divide by zero");
				}
				if (current.charAt(0) == '^' && leftOperand == 0 && rightOperand == 0) {
					scan.close();
					throw new ExpressionFormatException("0^0");
				}
				operandStack.push(compute(current.charAt(0))); // pushes the computation of the left and right operands
			}

		}
		scan.close();
		int result = operandStack.pop();
		if (!operandStack.isEmpty()) {
			throw new ExpressionFormatException("Too many operands");
		}
		return result;
	}

	/**
	 * For unary operator, pops the right operand from operandStack, and assign it
	 * to rightOperand. The stack must have at least one entry. Otherwise, throws
	 * NoSuchElementException. For binary operator, pops the right and left operands
	 * from operandStack, and assign them to rightOperand and leftOperand,
	 * respectively. The stack must have at least two entries. Otherwise, throws
	 * NoSuchElementException.
	 * 
	 * @param op char operator for checking if it is binary or unary operator.
	 */
	private void getOperands(char op) throws NoSuchElementException {
		if (op == '~') {
			if (operandStack.size() < 1) {
				throw new NoSuchElementException();
			}
			rightOperand = operandStack.pop();
		} else {
			if (operandStack.size() < 2) {
				throw new NoSuchElementException();
			}
			rightOperand = operandStack.pop();
			leftOperand = operandStack.pop();
		}
	}

	/**
	 * Computes "leftOperand op rightOperand" or "op rightOperand" if a unary
	 * operator.
	 * 
	 * @param op operator that acts on leftOperand and rightOperand.
	 * @return returns the value obtained by computation.
	 * @throws ExpressionFormatException with one of the messages below: <br>
	 *                                   -- "Divide by zero" if division is the
	 *                                   current operation and rightOperand == 0;
	 *                                   <br>
	 *                                   -- "0^0" if the current operation is "^"
	 *                                   and leftOperand == 0 and rightOperand == 0.
	 */
	private int compute(char op) throws ExpressionFormatException {
		if (op == '~') {
			return (-rightOperand);
		} else if (op == '+') {
			return leftOperand + rightOperand;
		} else if (op == '-') {
			return leftOperand - rightOperand;
		} else if (op == '*') {
			return leftOperand * rightOperand;
		} else if (op == '/') {
			if (rightOperand == 0) {
				throw new ExpressionFormatException("Divide by zero");
			}
			return leftOperand / rightOperand;
		} else if (op == '%') {
			return leftOperand % rightOperand;
		} else {
			if (leftOperand == 0 && rightOperand == 0) {
				throw new ExpressionFormatException("0^0");
			}
			return (int) Math.pow(leftOperand, rightOperand);
		}
	}
}
