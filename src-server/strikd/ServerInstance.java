package strikd;

import org.apache.log4j.Logger;

public class ServerInstance
{
	private final Logger logger = Logger.getLogger(this.getClass());
	
	public ServerInstance(String propertiesFile)
	{
		this.logger.info("starting");
	}
}
