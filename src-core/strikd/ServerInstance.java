package strikd;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.jongo.Jongo;

import com.mongodb.CommandResult;
import com.mongodb.MongoClient;

import strikd.communication.incoming.MessageHandlers;
import strikd.game.items.ItemShop;
import strikd.game.match.MatchManager;
import strikd.game.user.UserRegister;
import strikd.locale.LocaleBundleManager;
import strikd.net.NetListener;
import strikd.sessions.SessionManager;

public class ServerInstance
{
	private static final String version = "0.0.1-dev";
	private static final Logger logger = Logger.getLogger(ServerInstance.class);
	
	private final InstanceDescriptor instanceDescriptor;
	private final LocaleBundleManager localeMgr;
	private final Jongo dbCluster;
	private final NetListener listener;
	
	private final SessionManager sessionMgr;
	private final UserRegister playerRegister;
	private final MatchManager matchMgr;
	private final ItemShop shop;
	
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

		// Setup database
		try
		{
			Jongo cluster = new Jongo(new MongoClient(props.getProperty("db.server")).getDB(props.getProperty("db.name")));
			CommandResult stats = cluster.getDatabase().getStats();
			logger.info(String.format("db '%s' @ %s, col=%d, size=%f MiB",
							stats.get("db"),
							stats.get("serverUsed"),
							stats.getInt("collections"),
							((float)stats.getInt("dataSize") / 1024f / 1024f)));
			this.dbCluster = cluster;
		}
		catch(Exception e)
		{
			throw new Exception(String.format("could not connect to db '%s'", props.getProperty("db.name")), e);
		}
		
		// Load locale
		this.localeMgr = new LocaleBundleManager(new File(props.getProperty("locale.dir")));
		this.localeMgr.reload();
		
		// Setup registers and managers
		this.sessionMgr = new SessionManager();
		this.playerRegister = new UserRegister(this);
		this.matchMgr = new MatchManager(this);
		
		// Load shop assortment
		this.shop = new ItemShop();
		this.shop.reload();
		
		// Force message registry loading
		MessageHandlers.get(null);
		
		// Start accepting connections
		try
		{
			this.listener = new NetListener(13381, this);
		}
		catch(IOException e)
		{
			throw new Exception("could not start network server", e);
		}
		logger.info(String.format("listening on %s", this.listener.getLocalAddress()));
		
		// Print instance info
		this.instanceDescriptor = new InstanceDescriptor(this, props.getProperty("instance.name"));
		logger.info(String.format("this is instance %s", this.instanceDescriptor));
		
		// Start stats worker
		Executors.newSingleThreadScheduledExecutor().scheduleWithFixedDelay(new StatsWorker(this), 0, 1000, TimeUnit.MILLISECONDS);
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
	
	public String getVersion()
	{
		return ServerInstance.version;
	}
	
	public InstanceDescriptor getDescriptor()
	{
		return this.instanceDescriptor;
	}
	
	public LocaleBundleManager getLocaleMgr()
	{
		return this.localeMgr;
	}
	
	public Jongo getDbCluster()
	{
		return this.dbCluster;
	}
	
	public SessionManager getSessionMgr()
	{
		return this.sessionMgr;
	}
	
	public UserRegister getPlayerRegister()
	{
		return this.playerRegister;
	}

	public MatchManager getMatchMgr()
	{
		return this.matchMgr;
	}
	
	public ItemShop getShop()
	{
		return this.shop;
	}
	
	public boolean isShutdownMode()
	{
		return this.isShutdownMode;
	}
	
	public String getShutdownMessage()
	{
		return this.shutdownMessage;
	}
}
