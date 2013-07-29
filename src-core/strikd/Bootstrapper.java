package strikd;

import java.io.File;
import java.util.Arrays;

import org.apache.log4j.Logger;

public class Bootstrapper
{
	private static final Logger logger = Logger.getLogger(Bootstrapper.class);
	
	public static void main(String[] args)
	{
		logger.info("args=" + Arrays.toString(args));
		
		try
		{
			new Server(new File("strikd.properties"));
		}
		catch(Exception e)
		{
			logger.error("error starting instance", e);
			System.exit(-1);
		}
	}
}
