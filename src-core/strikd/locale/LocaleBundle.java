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
				boolean useIndexes = (type == DictionaryType.COMMON);
				String path = dictDir + File.separator + String.format(DICTIONARY_PATH, type.toString().toLowerCase());
				
				WordDictionary dict = new WordDictionary(locale, new File(path), useIndexes);
				logger.debug("loaded {} {} (words={} | index={})", locale, type, dict.size(), (useIndexes ? "yes" : "no"));
				
				this.dicts.put(type, dict);
			}
			catch(Exception ex)
			{
				logger.debug("{}: could not load dict for {}", locale, type);
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
