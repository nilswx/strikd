package strikd;

import java.util.Date;

import org.bson.types.ObjectId;
import org.jongo.marshall.jackson.oid.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class InstanceDescriptor
{
	@JsonIgnore
	private final ServerInstance instance;
	
	@Id
	public final ObjectId dbId = ObjectId.get();
	public final String name;
	public final Date started;
	
	public InstanceDescriptor(ServerInstance instance, String name)
	{
		this.instance = instance;
		this.name = name;
		this.started = new Date(System.currentTimeMillis());
	}
	
	@JsonProperty
	public String host()
	{
		return "serv-" + this.name.toLowerCase();
	}
	
	@JsonProperty
	public String version()
	{
		return this.instance.getVersion();
	}
	
	@JsonProperty
	public long memoryUsage()
	{
		// Retrieve current memory usage in bytes
		Runtime rt = Runtime.getRuntime();
		long bytes = (rt.totalMemory() - rt.freeMemory());
		
		// Return as megabytes
		return (bytes / 1024 / 1024);
	}
	
	@JsonProperty
	public int onlinePlayers()
	{
		return 0;
	}
	
	@JsonProperty
	public int activeMatches()
	{
		return this.instance.getMatchMgr().active();
	}
	
	@JsonProperty
	public int totalMatches()
	{
		return 999;
	}
	
	@JsonProperty
	public int avgWaitingTime()
	{
		return 12;
	}
	
	@JsonProperty
	public boolean online()
	{
		return !this.instance.isShutdownMode();
	}
	
	@Override
	public String toString()
	{
		return String.format("'%s' @ v%s (m=%d, mem=%d MiB)", this.name, this.version(), this.activeMatches(), this.memoryUsage());
	}
}