package strikd.game.util;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Stopwatch;

import net.firefang.ip2c.Country;
import net.firefang.ip2c.IP2Country;

public class CountryResolver
{
	private static final Logger logger = LoggerFactory.getLogger(CountryResolver.class);
	
	private static IP2Country database;

	public static void reload()
	{
		try
		{
			Stopwatch sw = new Stopwatch().start();
			database = new IP2Country(IP2Country.MEMORY_CACHE);
			sw.stop();
			
			logger.info("loaded database in {} ms", sw.elapsedMillis());
		}
		catch(IOException e)
		{
			logger.warn("cannot reload ip2country database, need a valid ip-to-country.bin in " + System.getProperty("user.dir"));
		}
		
		if(database == null)
		{
			logger.warn("country lookups will not be available");
		}
	}
	
	public static String getCountryCode(String ip)
	{
		if (database != null)
		{
			try
			{
				Country country = database.getCountry(ip);
				if(country == null)
				{
					logger.warn("{} -> ???", ip);
					return null;
				}
				else
				{
					String code = country.get2cStr().toLowerCase();
					logger.debug("{} -> '{}' ({})", ip, code, country.getName());
					return code;
				}
			}
			catch (IOException e)
			{
				logger.error("error resolving country for {}", ip, e);
			}
		}
		
		return null;
	}
}
