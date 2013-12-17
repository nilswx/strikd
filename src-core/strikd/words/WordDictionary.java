package strikd.words;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.Files;

import strikd.util.RandomUtil;
import strikd.words.index.WordDictionaryIndex;

public class WordDictionary
{
	private static final Logger logger = LoggerFactory.getLogger(WordDictionary.class);
	
	private static final String NORMALIZED_EXT = "snf"; // Strik Normalized File
	
	private final String locale;
	
	private final Word[] select;
	private final Set<String> check;
	
	private final WordDictionaryIndex index;
	
	public WordDictionary(String locale, File file, boolean useIndexes) throws IOException
	{
		// Determine whether to use indexes
		this.locale = locale;
		this.index = useIndexes ? new WordDictionaryIndex() : null;
		
		// Prepare lists
		this.check = new HashSet<String>();
		List<Word> list = new ArrayList<Word>();
		
		// Already normalized?
		String ext = ("." + Files.getFileExtension(file.getAbsolutePath()));
		File normalized = new File(file.getAbsolutePath().replace(ext, ("." + NORMALIZED_EXT)));
		
		// Need normalization?
		PrintWriter normalizeWriter = null;
		if(normalized.exists())
		{
			file = normalized;
		}
		else
		{
			try
			{
				normalizeWriter = new PrintWriter(normalized);
			}
			catch(Exception e)
			{
				logger.warn("could not create .{} file for {}", NORMALIZED_EXT, file, e);
			}
		}
		
		// Load and normalize words, one word per line
		String line;
		try(BufferedReader reader = new BufferedReader(new FileReader(file)))
		{
			while((line = reader.readLine()) != null)
			{
				// Normalize the word if needed
				Word word = (file == normalized ? new Word(line) : Word.parse(line));
				
				// Add to list
				list.add(word);
				this.check.add(word.toString());
				
				// Index into trie for generator/bots?
				if(useIndexes)
				{
					this.index.addWord(word.toString());
				}
				
				// Write to new file?
				if(normalizeWriter != null)
				{
					normalizeWriter.println(word.toString());
				}
			}
		}
		
		// Flush writer?
		if(normalizeWriter != null)
		{
			normalizeWriter.flush();
			normalizeWriter.close();
			
			logger.info("normalized words and created {}/{}", locale, normalized.getName());
		}
		
		// Convert list to array
		this.select = list.toArray(new Word[0]);
	}
	
	public Word pickOne()
	{
		return RandomUtil.pickOne(this.select);
	}
	
	public boolean contains(Word word)
	{
		return this.check.contains(word.toString());
	}
	
	public String getLocale()
	{
		return this.locale;
	}
	
	public WordDictionaryIndex getIndex()
	{
		return this.index;
	}
	
	public int size()
	{
		return this.select.length;
	}
}
