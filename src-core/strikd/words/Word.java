package strikd.words;

import java.util.regex.Pattern;

public final class Word
{
	private final String string;
	private final char[] letters;
	
	private Word(String value)
	{
		if(value == null || value.length() == 0)
		{
			throw new IllegalArgumentException("word cannot be blank or null");
		}
		
		this.string = value;
		this.letters = value.toCharArray();
	}
	
	public int length()
	{
		return this.letters.length;
	}
	
	public char[] letters()
	{
		return this.letters;
	}
	
	public boolean isPalindrome()
	{
		// FIXME: make more efficient
		return new StringBuilder(this.string).reverse().toString().equals(this.string);
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj instanceof Word)
		{
			return ((Word)obj).string.equals(this.string);
		}
		else if(obj instanceof String)
		{
			return ((String)obj).equals(this.string);
		}
		else
		{
			return false;
		}
	}
	
	@Override
	public int hashCode()
	{
		return this.string.hashCode();
	}
	
	@Override
	public String toString()
	{
		return this.string;
	}

	private static final Pattern NORMALIZER = Pattern.compile("[^A-Z]");
	
	private static String normalize(String str)
	{
		return NORMALIZER.matcher(str.toUpperCase()).replaceAll("").intern();
	}
	
	public static Word parse(String str)
	{
		return new Word(normalize(str));
	}
}
