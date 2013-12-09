package strikd.game.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum Platform
{
	UNKNOWN,
	IOS,
	ANDROID;
	
	private static final Logger logger = LoggerFactory.getLogger(Platform.class);
	
	public static Platform determinePlatform(String hardware, String osVersion)
	{
		// No parsing for now
		Platform result = IOS;
		
		// Tadaa!
		logger.debug("{} @ {} -> {}", hardware, osVersion, result);
		return result;
	}
}
