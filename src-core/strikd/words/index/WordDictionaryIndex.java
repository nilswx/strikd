package strikd.words.index;

import java.io.Serializable;

public class WordDictionaryIndex extends LetterNode implements Serializable
{
	private static final long serialVersionUID = 1L;

	public WordDictionaryIndex()
	{
		super('*');
	}
	
	public void addWord(String word)
	{
		this.addLetter(word.toCharArray(), 0);
	}
	
	public boolean containsWord(String word)
	{
		char[] letters = word.toCharArray();
		
		// Letter path exists?
		LetterNode letter = this;
		for(int i = 0; i < letters.length; i++)
		{
			// Path ends prematurely?
			if((letter = letter.node(letters[i])) == null)
			{
				return false;
			}
		}
		
		// Last letter marks the end of an actual word?
		return letter.isWordEnd();
	}
}
