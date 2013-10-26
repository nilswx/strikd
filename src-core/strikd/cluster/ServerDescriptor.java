package strikd.cluster;

import java.util.Date;

public class ServerDescriptor
{
	public int serverId;
	public String name;
	public String host;
	public int port;
	
	public boolean online;
	public Date started;
	public long memoryUsage;
	
	public int onlineUsers;
	public long totalLogins;
	public int activeMatches;
	public long totalMatches;
	public int avgWaitingTime;
	
	public String version;
}
