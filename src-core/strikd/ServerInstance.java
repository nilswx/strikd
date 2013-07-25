package strikd;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

import strikd.game.match.MatchManager;
import strikd.locale.LocaleBundleManager;

public class ServerInstance
{
	private static final String version = "0.0.1-dev";
	private static final Logger logger = Logger.getLogger(ServerInstance.class);
	
	private final ServerInstance.Descriptor descriptor;
	private final LocaleBundleManager localeMgr;
	private final MatchManager matchMgr;
	
	public ServerInstance(File propsFile) throws Exception
	{
		// Load startup configuration
		Properties props = new Properties();
		try
		{
			props.load(new FileInputStream(propsFile));
		}
		catch(IOException e)
		{
			throw new Exception(String.format("could not load %s", propsFile), e);
		}
		logger.info(String.format("loaded %d entries from %s", props.size(), propsFile));

		// Load locale
		this.localeMgr = new LocaleBundleManager(new File(props.getProperty("locale.dir")));
		this.localeMgr.reload();
		
		// Setup match manager
		this.matchMgr = new MatchManager();
		
		// Print instance info
		this.descriptor = new ServerInstance.Descriptor(props.getProperty("instance.name"), ServerInstance.version);
		logger.info(String.format("this is instance %s", this.descriptor));
	}
	
	public ServerInstance.Descriptor getDescriptor()
	{
		return this.descriptor;
	}
	
	public LocaleBundleManager getLocaleMgr()
	{
		return this.localeMgr;
	}

	public MatchManager getMatchMgr()
	{
		return this.matchMgr;
	}
	
	public class Descriptor
	{
		private final String name;
		private final String version;
		
		private Descriptor(String name, String version)
		{
			this.name = name;
			this.version = version;
		}
		
		public String getName()
		{
			return this.name;
		}
		
		public String getVersion()
		{
			return this.version;
		}
		
		public long activeMatches()
		{
			return ServerInstance.this.matchMgr.active();
		}
		
		public long getMemUsage()
		{
			// Retrieve current memory usage in bytes
			Runtime rt = Runtime.getRuntime();
			long bytes = (rt.totalMemory() - rt.freeMemory());
			
			// Return as megabytes
			return (bytes / 1024 / 1024);
		}
		
		@Override
		public String toString()
		{
			return String.format("'%s' @ %s (m=%d, mem=%d MiB)", this.name, this.version, this.activeMatches(), this.getMemUsage());
		}
	}

}
