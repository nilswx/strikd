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
	public final ObjectId dbId;
	public final String name;
	public final Date started;
	
	public InstanceDescriptor(ServerInstance instance, String name)
	{
		this.dbId = ObjectId.get();
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
		return this.instance.getSessionMgr().players();
	}
	
	@JsonProperty
	public long totalLogins()
	{
		return 0;
	}
	
	@JsonProperty
	public int activeMatches()
	{
		return this.instance.getMatchMgr().active();
	}
	
	@JsonProperty
	public long totalMatches()
	{
		return this.instance.getMatchMgr().matchCounter();
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
		return String.format("'%s' @ v%s (p=%d, m=%d, mem=%d MiB)", this.name, this.version(), this.onlinePlayers(), this.activeMatches(), this.memoryUsage());
	}
}