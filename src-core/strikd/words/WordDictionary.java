package strikd.words;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class WordDictionary
{
	private final String locale;
	
	private final Set<Word> set;
	private final Word[] array;
	
	public WordDictionary(String locale, File file) throws IOException
	{
		this.locale = locale;
		
		// Load and normalize words, one word per line
		String line;
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		Set<Word> set = new HashSet<Word>();
		while((line = reader.readLine()) != null)
		{
			Word word = Word.parse(line);
			set.add(word);
		}
		
		// Create immutable collections for all kinds of purposes
		this.set = Collections.unmodifiableSet(set);
		this.array = set.toArray(new Word[0]);
	}
	
	public Word pickOne()
	{
		Random rand = new Random();
		return this.array[rand.nextInt(this.array.length)];
	}
	
	public Word[] pick(int amount)
	{
		Random rand = new Random();
		
		Word[] words = new Word[amount];
		for(int i = 0; i < amount; i++)
		{
			words[i] = this.array[rand.nextInt(this.array.length)];
		}
		
		return words;
	}
	
	public boolean contains(Word word)
	{
		return this.set.contains(word);
	}
	
	public String getLocale()
	{
		return this.locale;
	}
	
	public int size()
	{
		return this.set.size();
	}
}
