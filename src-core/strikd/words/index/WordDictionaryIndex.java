package strikd.words.index;

public class WordDictionaryIndex extends LetterNode
{
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
		return this.containsWord(word.toCharArray());
	}
}
