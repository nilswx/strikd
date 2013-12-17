package strikd.locale;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import strikd.words.WordDictionary;

public class LocaleBundle
{
	private static final String DICTIONARY_PATH = "%s.txt";
	private static final Logger logger = LoggerFactory.getLogger(LocaleBundle.class);
	
	private final String locale;
	private final Map<DictionaryType, WordDictionary> dicts;
	
	public LocaleBundle(String locale, File dictDir)
	{
		this.locale = locale;
		
		this.dicts = new HashMap<DictionaryType, WordDictionary>();
		for(DictionaryType type : DictionaryType.values())
		{
			try
			{
				// Build indexes for this dictionary?
				boolean useIndexes = (type == DictionaryType.COMMON);
				
				// Locate file
				File file = new File(dictDir + File.separator + String.format(DICTIONARY_PATH, type.toString().toLowerCase()));
				if(file.exists())
				{
					WordDictionary dict = new WordDictionary(locale, file, useIndexes);
					logger.debug("loaded {} {} (words={} | index={})", locale, type, dict.size(), (useIndexes ? "yes" : "no"));
					
					this.dicts.put(type, dict);
				}
				else
				{
					logger.warn("{}: could not locate dict for {}", locale, type);
				}
			}
			catch(Exception e)
			{
				logger.error("{}: could not load dict for {}", locale, type, e);
			}
		}
	}
	
	public String getLocale()
	{
		return this.locale;
	}
	
	public WordDictionary getDictionary(DictionaryType type)
	{
		return this.dicts.get(type);
	}
	
	public int size()
	{
		return this.dicts.size();
	}
	
	@Override
	public String toString()
	{
		return this.locale;
	}
	
	public enum DictionaryType
	{
		// Essential
		COMPLETE,
		COMMON,
		
		// Extras etc
		SEASONAL,
		BRANDING
	}
}
