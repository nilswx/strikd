package strikd;

import java.io.File;

import org.apache.log4j.Logger;

public class Bootstrapper
{
	private static final Logger logger = Logger.getLogger(Bootstrapper.class);
	
	public static void main(String[] args)
	{
		try
		{
			ServerInstance server = new ServerInstance(new File("strikd.properties"));
			server.toString();
		}
		catch(Exception e)
		{
			logger.error("error starting instance", e);
			System.exit(-1);
		}
	}
}
