package calculator;

import java.util.ArrayList;
import java.util.Arrays;

/*
 * Main class of the project
 */
public class Main {
	/*
	 * The entry point of the application
	 */
	public static void main(String[] args) {
		// Only calculate the expression when there is exactly 1 argument
		if (args.length == 1) {
			// Split the expression from the argument to an array of Strings by the
			// whitespace to separate all parts of an expression
			// '\\s' is regular expression for whitespace
			String[] splitStrings = args[0].split("\\s");
			// Parse Strings array to an ArrayList of Strings for easy
			// handling later
			ArrayList<String> expressionStringsList = new ArrayList<String>(Arrays.asList(splitStrings));
			// Get a single instance of Calculator
			Calculator calc = Calculator.getCalculator();
			// Calculate the expression by calling calculate() function of Calculator
			// class and return the result after calculating
			int result = calc.calculate(expressionStringsList);
			// Print out the result
			System.out.println(result);
		} else {
			// Print out below message if the number of argument is other than 1
			System.out.println("The number of arguments is inappropriate");
		}
	}
}
