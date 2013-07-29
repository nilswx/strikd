package strikd.sessions;

import strikd.game.match.MatchPlayer;
import strikd.game.player.Player;
import strikd.net.NetConnection;
import strikd.net.codec.OutgoingMessage;

public class Session
{
	private final long sessionId;
	private final NetConnection connection;
	
	private Player player;
	private MatchPlayer matchPlayer;
	
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
	
	public boolean isInMatch()
	{
		return (this.matchPlayer != null);
	}
	
	public MatchPlayer getMatchPlayer()
	{
		return this.matchPlayer;
	}
}
