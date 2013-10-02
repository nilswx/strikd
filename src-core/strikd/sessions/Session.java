package strikd.sessions;

import org.apache.log4j.Logger;

import strikd.Server;
import strikd.game.match.Match;
import strikd.game.match.MatchPlayer;
import strikd.game.match.queues.PlayerQueue;
import strikd.game.user.User;
import strikd.net.NetConnection;
import strikd.net.codec.OutgoingMessage;

public class Session extends Server.Referent
{
	private static final Logger logger = Logger.getLogger(Session.class);
	
	private final long sessionId;
	private final NetConnection connection;
	private boolean isEnded;
	
	private User user;
	private MatchPlayer matchPlayer;
	private PlayerQueue.Entry queueEntry;
	
	public Session(long sessionId, NetConnection connection, Server server)
	{
		super(server);
		this.sessionId = sessionId;
		this.connection = connection;
	}
	
	public void end(String reason)
	{
		this.getServer().getSessionMgr().endSession(this.sessionId, reason);
	}
	
	public void onEnd()
	{
		if(!this.isEnded)
		{
			this.isEnded = true;
			this.connection.close();
			if(this.isLoggedIn())
			{
				this.onLogout();
			}
		}
	}
	
	public void onNetClose(String reason)
	{
		this.end(reason);
	}
	
	private void onLogout()
	{
		// Exit queue or match
		if(this.isInQueue())
		{
			this.exitQueue();
		}
		else if(this.isInMatch())
		{
			this.exitMatch();
		}
		
		// Save complete user object
		this.getServer().getUserRegister().saveUser(this.user);
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
	
	public void setUser(User user, String platform)
	{
		if(this.user == null)
		{
			this.user = user;
			this.user.platform = platform;
			this.getServer().getSessionMgr().completeLogin(this);
		}
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
	
	public void setMatchPlayer(MatchPlayer player)
	{
		// Leave the queue!
		if(this.queueEntry != null)
		{
			this.exitQueue();
		}
		
		// Leave the old match
		if(this.matchPlayer != null)
		{
			this.matchPlayer.leave();
		}
		
		// Set the new player reference
		this.matchPlayer = player;
	}
	
	public Match getMatch()
	{
		return this.matchPlayer.getMatch();
	}
	
	public void exitMatch()
	{
		this.setMatchPlayer(null);
	}

	public void exitQueue()
	{
		this.setQueueEntry(null);
	}
	
	public void setQueueEntry(PlayerQueue.Entry entry)
	{
		if(this.queueEntry != null)
		{
			this.queueEntry.exit();
		}
		this.queueEntry = entry;
	}
}
