package strikd.game.util;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

public class Localization
{
	private static final Logger logger = LoggerFactory.getLogger(Localization.class);
	
	private static final Map<String, Map<String, String>> entries = Maps.newHashMap();
	
	public static String localize(String locale, String text)
	{
		Map<String, String> loc = entries.get(locale);
		if(loc == null)
		{
			logger.warn("unknown locale '{}': \"{}\" will not be localized", locale, text);
		}
		else
		{
			String localized = loc.get(text);
			if(localized == null)
			{
				logger.warn("no localization for \"{}\"", text);
			}
			else
			{
				text = localized;
			}
		}
		
		return text;
	}
}
