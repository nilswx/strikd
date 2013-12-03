package strikd;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import strikd.cluster.ServerCluster;
import strikd.communication.incoming.MessageHandlers;
import strikd.facebook.FacebookManager;
import strikd.game.items.ItemTypeRegistry;
import strikd.game.items.shop.Shop;
import strikd.game.match.MatchManager;
import strikd.game.player.PlayerRegister;
import strikd.game.stream.ActivityStream;
import strikd.game.util.CountryResolver;
import strikd.locale.LocaleBundleManager;
import strikd.net.NetServer;
import strikd.net.security.DiffieHellman;
import strikd.sessions.SessionManager;
import strikd.util.MemoryWatchdog;
import strikd.util.NamedThreadFactory;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.config.DataSourceConfig;
import com.avaje.ebean.config.ServerConfig;

public class Server
{
	private static final Logger logger = LoggerFactory.getLogger(Server.class);
	
	private final EbeanServer db;
	private final ServerCluster serverCluster;
	private final LocaleBundleManager localeMgr;
	private final NetServer netServer;
	
	private final SessionManager sessionMgr;
	private final PlayerRegister playerRegister;
	private final MatchManager matchMgr;
	private final Shop shop;
	private final FacebookManager facebook;
	private final ActivityStream activityStream;
	
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
		logger.info("loaded {} entries from {}", props.size(), propsFile);

		// Test crypto
		DiffieHellman.testGenerator();
		
		// Setup persistence layer (thanks Ebean!)
		this.db = this.setupDb(props);
		
		// Load locale
		this.localeMgr = new LocaleBundleManager(new File(props.getProperty("locale.dir")));
		this.localeMgr.reload();
		
		// Setup registers and managers
		this.sessionMgr = new SessionManager(this);
		this.playerRegister = new PlayerRegister(this);
		this.matchMgr = new MatchManager(this);
		
		// Join server cluster
		this.serverCluster = new ServerCluster(this, props);
		
		// Load item register
		ItemTypeRegistry.allTypes();
		
		// Load shop assortment
		this.shop = new Shop(this);
		
		// Setup activity stream
		this.activityStream = new ActivityStream(this);
		
		// Create FB manager, share global config with the rest
		this.facebook = new FacebookManager(
				props.getProperty("facebook.page.id"),
				props.getProperty("facebook.app.ns"),
				props.getProperty("facebook.app.token"), this);
		FacebookManager.setSharedAppNamespace(this.facebook.getAppNamespace());
		FacebookManager.setSharedAppAccessToken(this.facebook.getAppAccessToken());
		
		// Force message registry loading
		MessageHandlers.load();
		
		// Load country database
		CountryResolver.reload();
		
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
		logger.info("listening on {}", this.netServer.getLocalAddress());
		
		// Print server info
		logger.info("SERVER ONLINE {}", this.serverCluster.getSelf());
		
		// Start sync worker
		int syncInterval = Integer.parseInt(props.getProperty("cluster.sync.interval", "5"));
		ScheduledExecutorService sync = Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory("Cluster Sync"));
		sync.scheduleWithFixedDelay(this.serverCluster, syncInterval, syncInterval, TimeUnit.SECONDS);
		sync.scheduleWithFixedDelay(new MemoryWatchdog(), 30, 30, TimeUnit.SECONDS);
	}
	
	private EbeanServer setupDb(Properties props)
	{
		// Register with singleton
		ServerConfig conf = new ServerConfig();
		conf.setName("db");
		conf.setDefaultServer(true); 
		
		// Configure data source
		DataSourceConfig ds = new DataSourceConfig();  
		ds.setUsername(props.getProperty("db.user"));  
		ds.setPassword(props.getProperty("db.password"));  
		ds.setUrl(props.getProperty("db.url"));
		ds.setDriver(props.getProperty("db.driver"));  
		if(props.containsKey("db.pool.min"))
		{
			ds.setMinConnections(Integer.parseInt(props.getProperty("db.pool.min")));
		}
		if(props.containsKey("db.pool.max"))
		{
			ds.setMaxConnections(Integer.parseInt(props.getProperty("db.pool.max")));
		}
		conf.setDataSourceConfig(ds);
		
		// DDL options  
		if("1".equals(props.getProperty("dev.db.reset")))
		{
			logger.warn("!!! REQUESTING DATABASE DDL RESET !!");
			conf.setDdlGenerate(true);  
			conf.setDdlRun(true);
		}
		
		return EbeanServerFactory.create(conf);
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
			logger.info("shutdown received (\"{}\")", message);
			
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
	
	public EbeanServer getDatabase()
	{
		return this.db;
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
	
	public ActivityStream getActivityStream()
	{
		return this.activityStream;
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
		
		public final EbeanServer getDatabase()
		{
			return this.server.getDatabase();
		}
	}
}
