package strikd.cluster;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.jongo.MongoCollection;

import strikd.Server;
import strikd.Version;

public class ServerCluster extends Server.Referent implements Runnable
{
	private static final Logger logger = Logger.getLogger(ServerCluster.class);
	
	private ServerDescriptor self;
	private Map<Integer, ServerDescriptor> servers;
	
	private final MongoCollection dbServers;
	
	public ServerCluster(Server server, Properties props)
	{
		super(server);
		this.servers = Collections.emptyMap();
		this.dbServers = server.getDbCluster().getCollection("servers");
		this.updateSelf(props);
	}
	
	private void updateSelf(Properties props)
	{
		// Boot?
		if(this.self == null)
		{
			// Retrieve/create descriptor
			int serverId = Integer.parseInt(props.getProperty("server.id"));
			this.self = this.dbServers.findOne("{_id:#}", serverId).as(ServerDescriptor.class);
			if(this.self == null)
			{
				this.self = new ServerDescriptor();
			}
			logger.info(String.format("joining as #%d ('%s')", serverId, props.get("server.name")));
		}
		Server server = this.getServer();
		
		// This data is only updated at startup
		if(props != null)
		{
			this.self.serverId = Integer.parseInt(props.getProperty("server.id"));
			this.self.name = props.getProperty("server.name");
			this.self.host = props.getProperty("server.host");
			this.self.port = Integer.parseInt(props.getProperty("server.port"));
			this.self.version = Version.getVersion();
			this.self.started = new Date();
		}
		
		// This data is updated periodically
		Runtime vm = Runtime.getRuntime();
		this.self.memoryUsage = ((vm.totalMemory() - vm.freeMemory()) / 1024f / 1024f);
		this.self.onlineUsers = server.getSessionMgr().sessions();
		this.self.totalLogins = server.getSessionMgr().totalLogins();
		this.self.activeMatches = server.getMatchMgr().active();
		this.self.totalMatches = server.getMatchMgr().matchCounter();
		this.self.avgWaitingTime = 12;
		this.self.lastUpdate = new Date();
		
		// Flush to database
		this.dbServers.save(this.self);
	}
	
	@Override
	public void run()
	{
		this.refresh();
	}
	
	public void refresh()
	{
		this.updateSelf(null);
		this.rediscover();
	}
	
	private void rediscover()
	{
		// Update self
		this.updateSelf(null);
		
		// Refresh data and detect new servers
		Map<Integer, ServerDescriptor> newMap = new HashMap<Integer, ServerDescriptor>();
		for(ServerDescriptor server : this.dbServers.find("{_id:{$ne:#}}", this.self.serverId).as(ServerDescriptor.class))
		{
			newMap.put(server.serverId, server);
			if(!this.servers.containsKey(server.serverId))
			{
				this.onDiscover(server);
			}
		}
		
		// Detect removed servers
		for(ServerDescriptor server : this.servers.values())
		{
			if(!newMap.containsKey(server.serverId))
			{
				this.onUndiscover(server);
			}
		}
		
		// Done!
		this.servers.clear();
		this.servers = newMap;
	}
	
	private void onDiscover(ServerDescriptor server)
	{
		logger.info(String.format("discovered server #%d ('%s') -> %s:%d", server.serverId, server.name, server.host, server.port));
	}
	
	private void onUndiscover(ServerDescriptor server)
	{
		logger.info(String.format("server #%d ('%s') went away", server.serverId, server.name));
	}
	
	public ServerDescriptor getSelf()
	{
		return this.self;
	}
	
	public ServerDescriptor getServerById(int serverId)
	{
		return this.servers.get(serverId);
	}
	
	public int size()
	{
		return this.servers.size();
	}
}
