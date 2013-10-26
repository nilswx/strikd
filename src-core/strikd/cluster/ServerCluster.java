package strikd.cluster;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jongo.MongoCollection;

import strikd.Server;

public class ServerCluster extends Server.Referent
{
	private static final Logger logger = Logger.getLogger(ServerCluster.class);
	
	private ServerDescriptor self;
	private Map<Integer, ServerDescriptor> servers;
	
	private final MongoCollection dbServers;
	
	public ServerCluster(Server server)
	{
		super(server);
		this.servers = Collections.emptyMap();
		this.dbServers = server.getDbCluster().getCollection("servers");
	}
	
	public void updateSelf()
	{
		
	}
	
	public void rediscover()
	{
		// Update self
		this.updateSelf();
		
		// Refresh data and detected new servers
		Map<Integer, ServerDescriptor> newMap = new HashMap<Integer, ServerDescriptor>();
		for(ServerDescriptor server : this.dbServers.find().as(ServerDescriptor.class))
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
		this.servers = newMap;
	}
	
	private void onDiscover(ServerDescriptor server)
	{
		logger.info(String.format("discovered server #%d ('%s') -> %s:%d", server.serverId, server.name, server.host, server.port));
	}
	
	private void onUndiscover(ServerDescriptor server)
	{
		logger.info(String.format("server #%d ('%s') went away"));
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
