package strikd;

import org.jongo.MongoCollection;

public class StatsWorker implements Runnable
{
	private InstanceDescriptor descriptor;
	private MongoCollection instances;
	
	public StatsWorker(ServerInstance instance)
	{
		this.descriptor = instance.getDescriptor();
		this.instances = instance.getDbCluster().getCollection("instances");
		this.deleteOldDescriptor();
	}
	
	private void deleteOldDescriptor()
	{
		this.instances.remove("{name: #}", this.descriptor.name);
	}
	
	@Override
	public void run()
	{
		this.instances.save(this.descriptor);
	}
}
