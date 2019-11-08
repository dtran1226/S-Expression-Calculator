package calculator;

/*
 * Util class which contains common static functions to be used in other classes,
 * which can be used without the need to initiate an instance of the Util class
 */
public class Util {
	/*
	 * The function to check if a String can be parsed into a number
	 */
	public static boolean tryParseInt(String stringToParse) {
		try {
			// If possible, return 'true'
			Integer.parseInt(stringToParse);
			return true;
		} catch (NumberFormatException nfException) {
			// Unless, return 'false'
			return false;
		}
	}
}
