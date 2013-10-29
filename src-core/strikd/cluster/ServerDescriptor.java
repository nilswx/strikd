package strikd.cluster;

import java.util.Date;

import org.jongo.marshall.jackson.oid.Id;

public class ServerDescriptor
{
	@Id
	public int serverId;
	public String name;
	public String host;
	public int port;
	
	public Date started;
	public Date lastUpdate;
	public float memoryUsage;
	
	public int onlineUsers;
	public long totalLogins;
	public int activeMatches;
	public long totalMatches;
	public int avgWaitingTime;
	
	public String version;
	
	@Override
	public String toString()
	{
		return String.format("#%d '%s' @ v%s (u=%d, m=%d, mem=%.2f MiB)", this.serverId, this.name, this.version, this.onlineUsers, this.activeMatches, this.memoryUsage);
	}
}
