package strikd;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.jongo.Jongo;

import com.mongodb.CommandResult;
import com.mongodb.MongoClient;

import strikd.cluster.ServerCluster;
import strikd.communication.incoming.MessageHandlers;
import strikd.facebook.FacebookManager;
import strikd.game.items.ItemType;
import strikd.game.items.shop.Shop;
import strikd.game.match.MatchManager;
import strikd.game.player.PlayerRegister;
import strikd.game.stream.EventStreamManager;
import strikd.locale.LocaleBundleManager;
import strikd.net.NetServer;
import strikd.net.security.DiffieHellman;
import strikd.sessions.SessionManager;
import strikd.util.MemoryWatchdog;
import strikd.util.NamedThreadFactory;

public class Server
{
	private static final Logger logger = Logger.getLogger(Server.class);
	
	private final Jongo dbCluster;
	private final ServerCluster serverCluster;
	private final LocaleBundleManager localeMgr;
	private final NetServer netServer;
	
	private final SessionManager sessionMgr;
	private final PlayerRegister playerRegister;
	private final MatchManager matchMgr;
	private final Shop shop;
	private final FacebookManager facebook;
	private final EventStreamManager eventStreamMgr;
	
	private boolean isShutdownMode;
	private String shutdownMessage;
	
	public Server(File propsFile) throws Exception
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

		// Test crypto
		DiffieHellman.testGenerator();
		
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
		this.sessionMgr = new SessionManager(this);
		this.playerRegister = new PlayerRegister(this);
		this.matchMgr = new MatchManager(this);
		
		// Join server cluster
		this.serverCluster = new ServerCluster(this, props);
		
		// Print item types
		ItemType.debugTypes();
		
		// Load shop assortment
		this.shop = new Shop(this);
		
		// Setup event manager
		this.eventStreamMgr = new EventStreamManager(this);
		this.eventStreamMgr.reloadNews();
		
		// Create FB manager, share global config with the rest
		this.facebook = new FacebookManager(
				props.getProperty("facebook.page.id"),
				props.getProperty("facebook.app.ns"),
				props.getProperty("facebook.app.token"), this);
		FacebookManager.setSharedAppNamespace(this.facebook.getAppNamespace());
		FacebookManager.setSharedAppAccessToken(this.facebook.getAppAccessToken());
		
		// Force message registry loading
		MessageHandlers.get(null);
		
		// Start accepting connections
		try
		{
			int port = Integer.parseInt(props.getProperty("server.port", "13381"));
			this.netServer = new NetServer(port, this.sessionMgr);
		}
		catch(IOException e)
		{
			throw new Exception("could not start network server", e);
		}
		logger.info(String.format("listening on %s", this.netServer.getLocalAddress()));
		
		// Print server info
		logger.info(String.format("SERVER ONLINE %s", this.serverCluster.getSelf()));
		
		// Start sync worker
		int syncInterval = Integer.parseInt(props.getProperty("cluster.sync.interval", "5"));
		ScheduledExecutorService sync = Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory("Cluster Sync"));
		sync.scheduleAtFixedRate(this.serverCluster, syncInterval, syncInterval, TimeUnit.SECONDS);
		sync.scheduleAtFixedRate(new MemoryWatchdog(), 30, 30, TimeUnit.SECONDS);
	}

	public void destroy()
	{
		this.destroy(false);
	}
	
	private void destroy(boolean force)
	{
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
		if(!this.isShutdownMode)
		{
			// Set shutdown mode so it can't be triggered twice in a row
			this.isShutdownMode = true;
			this.shutdownMessage = message;
			logger.info(String.format("shutdown received (\"%s\")", message));
			
			// Can safely shutdown right now?
			if(this.matchMgr.active() == 0)
			{
				this.shutdownNow();
			}
			else
			{
				this.matchMgr.shutdownQueues(message);
			}
		}
	}
	
	public void shutdownNow()
	{
		this.destroy(true);
	}
	
	public Jongo getDbCluster()
	{
		return this.dbCluster;
	}
	
	public ServerCluster getServerCluster()
	{
		return this.serverCluster;
	}
	
	public LocaleBundleManager getLocaleMgr()
	{
		return this.localeMgr;
	}
	
	public SessionManager getSessionMgr()
	{
		return this.sessionMgr;
	}
	
	public PlayerRegister getPlayerRegister()
	{
		return this.playerRegister;
	}

	public MatchManager getMatchMgr()
	{
		return this.matchMgr;
	}
	
	public Shop getShop()
	{
		return this.shop;
	}
	
	public EventStreamManager getEventStreamMgr()
	{
		return this.eventStreamMgr;
	}
	
	public FacebookManager getFacebook()
	{
		return this.facebook;
	}
	
	public boolean isShutdownMode()
	{
		return this.isShutdownMode;
	}
	
	public String getShutdownMessage()
	{
		return this.shutdownMessage;
	}
	
	public static abstract class Referent
	{
		private final Server server;
		
		public Referent(Server server)
		{
			this.server = server;
		}
		
		public final Server getServer()
		{
			return this.server;
		}
	}
}
