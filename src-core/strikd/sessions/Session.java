package strikd.sessions;

import strikd.game.match.MatchPlayer;
import strikd.game.match.queues.PlayerQueue;
import strikd.game.player.Player;
import strikd.net.NetConnection;
import strikd.net.codec.OutgoingMessage;

public class Session
{
	private final long sessionId;
	private final NetConnection connection;
	
	private Player player;
	private MatchPlayer matchPlayer;
	private PlayerQueue.Entry queueEntry;
	
	public Session(long sessionId, NetConnection connection)
	{
		this.sessionId = sessionId;
		this.connection = connection;
		
		this.player = new Player();
		//this.player.id = ObjectId.get();
		this.player.name = "nilsw";
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
		return (this.player != null);
	}
	
	public Player getPlayer()
	{
		return this.player;
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

	public void setQueueEntry(PlayerQueue.Entry entry)
	{
		if(this.isInQueue())
		{
			this.queueEntry.cancel();
		}
		this.queueEntry = entry;
	}
}
