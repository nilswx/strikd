package strikd.game.words;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class WordDictionary
{
	private final String language;
	private final Set<String> words;
	
	public WordDictionary(String language, File file) throws IOException
	{
		this.language = language;
		
		// Load and normalize words, one word per line
		String line;
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		Set<String> words = new HashSet<String>();
		while((line = reader.readLine()) != null)
		{
			String word = WordNormalizer.normalize(line);
			words.add(word);
		}
		
		this.words = Collections.unmodifiableSet(words);
	}
	
	public boolean contains(String word)
	{
		return this.words.contains(word);
	}
	
	public String getLanguage()
	{
		return this.language;
	}
}
