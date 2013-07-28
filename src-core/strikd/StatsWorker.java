package strikd;

import org.jongo.MongoCollection;

public class StatsWorker implements Runnable
{
	private InstanceDescriptor descriptor;
	private MongoCollection dbInstances;
	
	public StatsWorker(ServerInstance instance)
	{
		this.descriptor = instance.getDescriptor();
		this.dbInstances = instance.getDbCluster().getCollection("instances");
		this.deleteOldDescriptor();
	}
	
	private void deleteOldDescriptor()
	{
		this.dbInstances.remove("{name: #}", this.descriptor.name);
	}
	
	@Override
	public void run()
	{
		this.dbInstances.save(this.descriptor);
	}
}
