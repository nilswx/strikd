package strikd.cluster;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity @Table(name="servers")
public class ServerDescriptor
{
	@Id
	private int id;
	private String name;
	private String host;
	private int port;
	private Date started;
	private float memoryUsage;
	private int onlinePlayers;
	private long totalLogins;
	private int activeMatches;
	private long totalMatches;
	private int avgWaitingTime;
	private String version;
	private Date lastUpdate;
	
	@Version
	private int heartbeat;
	
	public int getId()
	{
		return this.id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getName()
	{
		return this.name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getHost()
	{
		return this.host;
	}

	public void setHost(String host)
	{
		this.host = host;
	}

	public int getPort()
	{
		return this.port;
	}

	public void setPort(int port)
	{
		this.port = port;
	}

	public Date getStarted()
	{
		return this.started;
	}

	public void setStarted(Date started)
	{
		this.started = started;
	}

	public float getMemoryUsage()
	{
		return this.memoryUsage;
	}

	public void setMemoryUsage(float memoryUsage)
	{
		this.memoryUsage = memoryUsage;
	}

	public int getOnlinePlayers()
	{
		return this.onlinePlayers;
	}

	public void setOnlinePlayers(int onlinePlayers)
	{
		this.onlinePlayers = onlinePlayers;
	}

	public long getTotalLogins()
	{
		return this.totalLogins;
	}

	public void setTotalLogins(long totalLogins)
	{
		this.totalLogins = totalLogins;
	}

	public int getActiveMatches()
	{
		return this.activeMatches;
	}

	public void setActiveMatches(int activeMatches)
	{
		this.activeMatches = activeMatches;
	}

	public long getTotalMatches()
	{
		return this.totalMatches;
	}

	public void setTotalMatches(long totalMatches)
	{
		this.totalMatches = totalMatches;
	}

	public int getAvgWaitingTime()
	{
		return this.avgWaitingTime;
	}

	public void setAvgWaitingTime(int avgWaitingTime)
	{
		this.avgWaitingTime = avgWaitingTime;
	}

	public String getVersion()
	{
		return this.version;
	}

	public void setVersion(String version)
	{
		this.version = version;
	}

	public Date getLastUpdate()
	{
		return this.lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate)
	{
		this.lastUpdate = lastUpdate;
	}

	public int getHeartbeat()
	{
		return this.heartbeat;
	}

	public void setHeartbeat(int heartbeat)
	{
		this.heartbeat = heartbeat;
	}

	@Override
	public String toString()
	{
		return String.format("#%d '%s' @ v%s (p=%d m=%d mem=%.2f MiB)", this.id, this.name, this.version, this.onlinePlayers, this.activeMatches, this.memoryUsage);
	}
}
