package calculator;

import java.util.ArrayList;

/*
 * Calculator class, which executes calculating processes
 * This class is a singleton class since there is no need to create many calculators,
 * one calculator is enough to perform all calculating processes
 */
public class Calculator {

	// Variables of the this class (3 parts of an operation), which are set to
	// private to prevent accessing directly from other classes
	private String operator;
	private int leftOperand;
	private int rightOperand;
	private int result;
	// The single instance of this class, default is null
	private static Calculator calculator = null;

	/*
	 * Public getters, setters, which are used to access above variables
	 */
	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public int getLeftOperand() {
		return leftOperand;
	}

	public void setLeftOperand(int leftOperand) {
		this.leftOperand = leftOperand;
	}

	public int getRightOperand() {
		return rightOperand;
	}

	public void setRightOperand(int rightOperand) {
		this.rightOperand = rightOperand;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	/*
	 * Private default constructor to set default values for variables of this
	 * class, which prevents other classes to create new instance of this class
	 */
	private Calculator() {
		super();
		this.operator = "";
		this.leftOperand = 0;
		this.rightOperand = 0;
		this.result = 0;
	}

	/*
	 * Public static function getCalculator(), which allows other classes to get a
	 * single instance of this class
	 */
	public static Calculator getCalculator() {
		// If an instance of this class does not exist, create one
		// Unless, return the current one, which has been initiated before
		if (calculator == null) {
			calculator = new Calculator();
		}
		return calculator;
	}

	/*
	 * Calculate an expression, which is in form of an ArrayList of Strings then
	 * return the result of the expression after calculating
	 */
	public int calculate(ArrayList<String> expressionStrings) {
		// The idea of this process is:
		// - Loop through the list of expressionStrings
		// - If found any chain of consecutive numbers, in which the last number
		// contains ')' character (it forms a complete operation)-> calculate those
		// numbers with the operator is determined right before the first number of
		// those chains
		// - Replace those operations with the calculated result in the
		// expressionStrings list
		// - Keep looping through the expressionStrings list and doing the same steps as
		// above until the list size equals 1 which contains the result only
		// - Return the result from the first element of the expressionStrings list
		try {
			// Keep calculating the expression until getting the final result (the list
			// size equals 1 which contains the result only)
			while (expressionStrings.size() > 1) {
				// Loop through the expressionStrings list
				for (int i = 0; i < expressionStrings.size(); i++) {
					// Get the String at position 'i' of the list, then remove all ')' characters to
					// get pure number
					// '\\' is placed before ')' because ')' is the special character and
					// replaceAll() requires a regular expression
					// ')' characters are removed because they are redundant from operands
					String currentString = expressionStrings.get(i);
					currentString = currentString.replaceAll("\\)", "");
					// If String at position 'i' is a number
					if (Util.tryParseInt(currentString)) {
						// Set the leftOperand to its value
						leftOperand = Integer.parseInt(currentString);
						// Get value of the String at next position
						currentString = expressionStrings.get(++i);
						// Keep currentString's value before removes ')' to check if it contains ')'
						String originalCurrentString = currentString;
						currentString = currentString.replaceAll("\\)", "");
						// Set default operator position to '-1' as undetermined
						int operatorPosition = -1;
						// Keep calculating the expression if there is at least 1 more number right
						// after the current String
						while (Util.tryParseInt(currentString)) {
							// Determine the position of the operator for the
							// current operation in the expressionStrings list when it is undetermined.
							if (operatorPosition < 0) {
								// The operator is undetermined only at the first it comes to this 'while' block
								// (the second number of a chain of consecutive numbers)
								// Since the operator must be right before the first number of that chain, that
								// is why it equals 'i-2'
								operatorPosition = i - 2;
							}
							// Set the rightOperand to currentString's value
							rightOperand = Integer.parseInt(currentString);
							// Set the operator to operatorPosition's value from the expressionStrings list
							// of the operation
							operator = expressionStrings.get(operatorPosition);
							// '(' characters are removed because they are redundant from
							// operators
							operator = operator.replaceAll("\\(", "");
							// Whenever an operation is detected (found at least 2 consecutive numbers and
							// an operator), calculate it first
							calculate();
							// A variable to keep track the number of ')' characters in the current String
							// Default value is 0
							int count = 0;
							// Count the number of ')' characters in the current String
							while (originalCurrentString.contains(")")) {
								count++;
								originalCurrentString = originalCurrentString.replaceFirst("\\)", "");
							}
							// If there is ')' character in the current String (it forms a complete
							// operation)
							if (count > 0) {
								/*
								 * Simplify the expression by replacing the detected operation which are stored
								 * in adjacent indexes of the expressionStrings list by the above calculated
								 * result
								 */
								// Add ')' characters back to the current String if it contains more than 1 ')'
								// characters
								String strResult = String.valueOf(result);
								for (int j = 0; j < count - 1; j++) {
									strResult += ")";
								}
								// Set the value at the detected operator's index of the expressionStrings list
								// to the calculated result
								expressionStrings.set(operatorPosition, strResult);
								// Remove the detected operands from the list
								for (int k = operatorPosition + 1; k <= i; k++) {
									// Remove the element at 'operationPosition + 1' instead of 'k' since after
									// removing an element from a list, next elements will move 1 spot to the left
									// of the list
									expressionStrings.remove(operatorPosition + 1);
								}
								// Move to the first position of the expressionStrings list to restart looping
								// after finding an operation
								i = 0;
								// Stop looking for consecutive numbers for current operation since it has done
								break;
							} else {
								// Keep looking for more consecutive numbers
								// Get value of the String at next position
								currentString = expressionStrings.get(++i);
								// Keep currentString's value before removes ')' to check if it contains ')'
								originalCurrentString = currentString;
								currentString = currentString.replaceAll("\\)", "");
								// Accumulate result of consecutive numbers and assign them to leftOperand
								leftOperand = result;
							}

						}
					}
				}
			}
		} catch (IndexOutOfBoundsException ioobException) {
			System.out.println("Inappropriate arguments format");
		}
		// After each time looping through the list to simplify the expression, there
		// are some operands are removed from the list so the list size is gradually
		// reduced
		// The calculating process is done when the list size becomes 1, which contains
		// the final result only. Unless, keep calculating if the list size is greater
		// than 1
		// Return the final result at the first index of the list
		if (Util.tryParseInt(expressionStrings.get(0))) {
			result = Integer.parseInt(expressionStrings.get(0));
		}
		return result;
	}

	/*
	 * Calculate a simple operation, which includes an operator and 2 operands. This
	 * function is set to private to prevent other classes to call. It is a
	 * sub-function to support for the calculate expression function only
	 */
	private void calculate() {
		switch (operator) {
		// Add 2 operands when it is the 'add' operator
		case "add":
			result = leftOperand + rightOperand;
			break;
		// Multiply 2 operands when it is the 'multiply' operator
		case "multiply":
			result = leftOperand * rightOperand;
			break;
		// Easy add more operators here, like 'exponent', 'divide', 'subtract', etc.
		default:
			break;
		}
	}
}
