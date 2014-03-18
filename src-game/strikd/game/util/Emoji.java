package strikd.game.util;

public class Emoji
{
	public static String getRandomEmoji()
	{
		// Get a random emoji codepoint (unicode)
		int codePoint = 0x1F435; // MONKEY
		
		// Retrieve characters and create string
		return new String(Character.toChars(codePoint));
	}
}
