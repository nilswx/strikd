package strikd;

import java.util.Date;

import org.bson.types.ObjectId;
import org.jongo.marshall.jackson.oid.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ServerDescriptor
{
	@JsonIgnore
	private final Server server;
	
	@Id
	public final ObjectId dbId;
	public final String name;
	public final Date started;
	
	public ServerDescriptor(Server server, String name)
	{
		this.dbId = ObjectId.get();
		this.server = server;
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
		return this.server.getVersion();
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
	public int onlineUsers()
	{
		return this.server.getSessionMgr().users();
	}
	
	@JsonProperty
	public long totalLogins()
	{
		return 0;
	}
	
	@JsonProperty
	public int activeMatches()
	{
		return this.server.getMatchMgr().active();
	}
	
	@JsonProperty
	public long totalMatches()
	{
		return this.server.getMatchMgr().matchCounter();
	}
	
	@JsonProperty
	public int avgWaitingTime()
	{
		return 12;
	}
	
	@JsonProperty
	public boolean online()
	{
		return !this.server.isShutdownMode();
	}
	
	@Override
	public String toString()
	{
		return String.format("'%s' @ v%s (u=%d, m=%d, mem=%d MiB)", this.name, this.version(), this.onlineUsers(), this.activeMatches(), this.memoryUsage());
	}
}