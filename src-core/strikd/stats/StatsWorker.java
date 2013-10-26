package strikd.stats;

import org.jongo.MongoCollection;

import strikd.Server;
import strikd.ServerDescriptor;

public class StatsWorker implements Runnable
{
	private ServerDescriptor descriptor;
	private MongoCollection dbServers;
	
	public StatsWorker(Server server)
	{
		this.descriptor = server.getDescriptor();
		this.dbServers = server.getDbCluster().getCollection("servers");
		this.deleteOldDescriptor();
	}
	
	private void deleteOldDescriptor()
	{
		this.dbServers.remove("{name: #}", this.descriptor.name);
	}
	
	@Override
	public void run()
	{
		this.dbServers.save(this.descriptor);
	}
}
