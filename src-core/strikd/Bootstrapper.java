package strikd;

import java.io.File;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Bootstrapper
{
	private static final Logger logger = LoggerFactory.getLogger(Bootstrapper.class);
	
	public static void main(String[] args)
	{
		logger.info("args=" + Arrays.toString(args));
		
		try
		{
			logger.info("starting server");
			Server instance = new Server(new File("strikd.properties"));
			instance.toString();
		}
		catch(Exception e)
		{
			logger.error("error starting instance", e);
			System.exit(-1);
		}
	}
}
