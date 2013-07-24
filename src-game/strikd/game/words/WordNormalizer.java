package strikd.game.words;

import java.util.regex.Pattern;

public class WordNormalizer
{
	private static final Pattern PATTERN = Pattern.compile("[^A-Z]");
	
	public static String normalize(String word)
	{
		return PATTERN.matcher(word.toUpperCase()).replaceAll("");
	}
}
