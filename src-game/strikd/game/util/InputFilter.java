package strikd.game.util;

public class InputFilter
{
	public static String sanitizeInput(String input)
	{
		// Sanitize it
		input.replace("fuck", "");
		
		return input;
	}
}
