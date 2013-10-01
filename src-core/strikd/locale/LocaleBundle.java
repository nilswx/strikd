package strikd.locale;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import strikd.words.WordDictionary;

public class LocaleBundle
{
	private static final String DICTIONARY_PATH = "%s.txt";
	private static final Logger logger = Logger.getLogger(LocaleBundle.class);
	
	private final String locale;
	private final Map<DictionaryType, WordDictionary> dicts;
	
	public LocaleBundle(String locale, File dictDir)
	{
		this.locale = locale;
		
		// Load dictionaries
		Map<DictionaryType, WordDictionary> dicts = new HashMap<DictionaryType, WordDictionary>();
		for(DictionaryType type : DictionaryType.values())
		{
			try
			{
				String path = dictDir + File.separator + String.format(DICTIONARY_PATH, type.toString().toLowerCase());
				dicts.put(type, new WordDictionary(locale, new File(path)));
			}
			catch(Exception ex)
			{
				logger.debug(String.format("%s: could not load dict for %s", locale, type));
			}
		}
		
		this.dicts = Collections.unmodifiableMap(dicts);
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
		GENERATOR,
		
		// Extras etc
		SEASONAL,
		BRANDING
	}
}
