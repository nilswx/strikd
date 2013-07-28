package strikd;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

import strikd.game.match.MatchManager;
import strikd.locale.LocaleBundleManager;
import strikd.net.NetListener;
import strikd.sessions.SessionManager;

public class ServerInstance
{
	private static final String version = "0.0.1-dev";
	private static final Logger logger = Logger.getLogger(ServerInstance.class);
	
	private final ServerInstance.Descriptor descriptor;
	private final LocaleBundleManager localeMgr;
	
	private final NetListener gameListener;
	private final SessionManager sessionMgr;
	private final MatchManager matchMgr;
	
	private boolean isShutdownMode;
	private String shutdownMessage;
	
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
		
		// Setup session manager
		this.sessionMgr = new SessionManager();
		
		// Setup match manager
		this.matchMgr = new MatchManager(this);
		
		// Start accepting connections
		this.gameListener = new NetListener(13381);
		
		// Print instance info
		this.descriptor = new ServerInstance.Descriptor(props.getProperty("instance.name"), ServerInstance.version);
		logger.info(String.format("this is instance %s", this.descriptor));
	}

	public void destroy()
	{
		this.destroy(false);
	}
	
	private void destroy(boolean force)
	{
		// No active matches?
		if(this.matchMgr.active() > 0 && !force)
		{
			logger.warn("destroy() called while there were active matches");
		}
		else
		{
			logger.info("destroying process");
			System.exit(0);
		}
	}
	
	public void shutdown(String message)
	{
		if(this.matchMgr.active() == 0)
		{
			this.shutdownNow();
		}
		else
		{
			this.isShutdownMode = true;
			this.shutdownMessage = message;
		}
	}
	
	public void shutdownNow()
	{
		this.destroy(true);
	}
	
	public ServerInstance.Descriptor getDescriptor()
	{
		return this.descriptor;
	}
	
	public LocaleBundleManager getLocaleMgr()
	{
		return this.localeMgr;
	}
	
	public SessionManager getSessionMgr()
	{
		return this.sessionMgr;
	}

	public MatchManager getMatchMgr()
	{
		return this.matchMgr;
	}
	
	public boolean isShutdownMode()
	{
		return this.isShutdownMode;
	}
	
	public String getShutdownMessage()
	{
		return this.shutdownMessage;
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
			return String.format("'%s' @ v%s (m=%d, mem=%d MiB)", this.name, this.version, this.activeMatches(), this.getMemUsage());
		}
	}
}
