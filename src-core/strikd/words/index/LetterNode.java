package strikd.words.index;

import java.io.Serializable;

public class LetterNode implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private static final int ALPHABET_LENGTH = 26;
	private static final char ALPHABET_OFFSET = 'A';
	
	private final char letter;
	private final LetterNode[] letters;
	private boolean isWordEnd;
	
	public LetterNode(char letter)
	{
		this.letter = letter;
		this.letters = new LetterNode[ALPHABET_LENGTH];
	}
		
	protected void addLetter(char[] letters, int pos)
	{
		if(pos < letters.length)
		{
			this.getLetter(letters[pos]).addLetter(letters, (pos + 1));
		}
		else
		{
			this.isWordEnd = true;
		}
	}
	
	private LetterNode getLetter(char letter)
	{
		LetterNode node = this.letters[letterIndex(letter)];
		if(node == null)
		{
			node = this.letters[letterIndex(letter)] = new LetterNode(letter);
		}
		
		return node;
	}
	
	public LetterNode node(char letter)
	{
		return (this.letters[letterIndex(letter)]);
	}
	
	public boolean isWordEnd()
	{
		return this.isWordEnd;
	}
	
	@Override
	public String toString()
	{
		return Character.toString(this.letter);
	}
	
	private static final int letterIndex(final char letter)
	{
		return (letter - ALPHABET_OFFSET);
	}
}
