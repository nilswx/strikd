package strikd.sessions;

import strikd.Server;
import strikd.game.match.MatchPlayer;
import strikd.game.match.queues.PlayerQueue;
import strikd.game.user.User;
import strikd.net.NetConnection;
import strikd.net.codec.OutgoingMessage;

public class Session extends Server.Referent
{
	private final long sessionId;
	private final NetConnection connection;
	
	private User user;
	private MatchPlayer matchPlayer;
	private PlayerQueue.Entry queueEntry;
	
	public Session(long sessionId, NetConnection connection, Server server)
	{
		super(server);
		this.sessionId = sessionId;
		this.connection = connection;
	}
	
	public void send(OutgoingMessage msg)
	{
		this.connection.send(msg);
	}
	
	public long getSessionId()
	{
		return this.sessionId;
	}
	
	public NetConnection getConnection()
	{
		return this.connection;
	}
	
	public boolean isLoggedIn()
	{
		return (this.user != null);
	}
	
	public User getUser()
	{
		return this.user;
	}
	
	public boolean isInQueue()
	{
		return (this.queueEntry != null);
	}
	
	public boolean isInMatch()
	{
		return (this.matchPlayer != null);
	}
	
	public MatchPlayer getMatchPlayer()
	{
		return this.matchPlayer;
	}

	public void exitQueue()
	{
		this.setQueueEntry(null);
	}
	
	public void setQueueEntry(PlayerQueue.Entry entry)
	{
		if(this.isInQueue())
		{
			this.queueEntry.exit();
		}
		this.queueEntry = entry;
	}
}
