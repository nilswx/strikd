package strikd.words;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class WordDictionary
{
	private final String locale;
	
	private final Word[] select;
	private final Set<String> check;
	
	public WordDictionary(String locale, File file) throws IOException
	{
		this.locale = locale;
		
		// Load and normalize words, one word per line
		String line;
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		List<Word> select = new ArrayList<Word>();
		Set<String> check = new HashSet<String>();
		while((line = reader.readLine()) != null)
		{
			Word word = Word.parse(line);
			select.add(word);
			check.add(word.toString());
		}
		
		// Create immutable collections for all kinds of purposes
		this.select = select.toArray(new Word[0]);
		this.check = check;
	}
	
	public Word pickOne()
	{
		Random rand = new Random();
		return this.select[rand.nextInt(this.select.length)];
	}
	
	public Word[] pick(int amount)
	{
		Random rand = new Random();
		
		Word[] words = new Word[amount];
		for(int i = 0; i < amount; i++)
		{
			words[i] = this.select[rand.nextInt(this.select.length)];
		}
		
		return words;
	}
	
	public boolean contains(Word word)
	{
		return this.check.contains(word.toString());
	}
	
	public String getLocale()
	{
		return this.locale;
	}
	
	public int size()
	{
		return this.select.length;
	}
}
