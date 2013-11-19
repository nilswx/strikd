package strikd.cluster;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import strikd.Server;
import strikd.Version;

public class ServerCluster extends Server.Referent implements Runnable
{
	private static final Logger logger = LoggerFactory.getLogger(ServerCluster.class);
	
	private ServerDescriptor self;
	private Map<Integer, ServerDescriptor> servers;
	
	public ServerCluster(Server server, Properties props)
	{
		super(server);
		this.servers = Collections.emptyMap();
		this.joinCluster(props);
	}
	
	private void joinCluster(Properties props)
	{
		// Retrieve/create descriptor
		int serverId = Integer.parseInt(props.getProperty("server.id"));
		this.self = this.getDatabase().find(ServerDescriptor.class, serverId);
		if(this.self == null)
		{
			this.self = this.getDatabase().createEntityBean(ServerDescriptor.class);//new ServerDescriptor();
		}
		
		// Update with latest startup data
		this.self.setId(serverId);
		this.self.setName(props.getProperty("server.name"));
		this.self.setHost(props.getProperty("server.host"));
		this.self.setPort(Integer.parseInt(props.getProperty("server.port")));
		this.self.setVersion(Version.getVersion());
		this.self.setStarted(new Date());
		this.syncSelf();
		
		// Output to logs
		logger.info(String.format("joining as server #%d (%s:%d)", serverId, this.self.getHost(), this.self.getPort()));
	}
	
	private void syncSelf()
	{
		// This data is updated periodically
		Server server = this.getServer();
		Runtime vm = Runtime.getRuntime();
		this.self.setMemoryUsage((vm.totalMemory() - vm.freeMemory()) / 1024f / 1024f);
		this.self.setOnlinePlayers(server.getSessionMgr().players());
		this.self.setTotalLogins(server.getSessionMgr().totalLogins());
		this.self.setActiveMatches(server.getMatchMgr().active());
		this.self.setTotalMatches(server.getMatchMgr().matchCounter());
		this.self.setAvgWaitingTime(12);
		
		// Flush to database
		this.getDatabase().save(this.self);
	}
	
	@Override
	public void run()
	{
		try
		{
			this.refresh();
		}
		catch(Exception ex)
		{
			logger.error(String.format("error refreshing server #%d", this.self.getId()), ex);
		}
	}
	
	private void refresh()
	{
		this.syncSelf();
		this.rediscover();
	}
	
	private void rediscover()
	{
		// Refresh data and detect new servers
		Map<Integer, ServerDescriptor> newMap = new HashMap<Integer, ServerDescriptor>();
		for(ServerDescriptor server : this.getDatabase().find(ServerDescriptor.class).findList())
		{
			// Not self?
			if(server.getId() != this.self.getId())
			{
				newMap.put(server.getId(), server);
				if(!this.servers.containsKey(server.getId()))
				{
					this.onDiscover(server);
				}
			}
		}
		
		// Detect removed servers
		for(ServerDescriptor server : this.servers.values())
		{
			if(!newMap.containsKey(server.getId()))
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
		logger.info(String.format("discovered server #%d ('%s') -> %s:%d", server.getId(), server.getName(), server.getHost(), server.getPort()));
	}
	
	private void onUndiscover(ServerDescriptor server)
	{
		logger.info(String.format("server #%d ('%s') went away", server.getId(), server.getName()));
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
