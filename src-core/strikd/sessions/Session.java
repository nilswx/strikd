package strikd.sessions;

import java.util.UUID;

import org.apache.log4j.Logger;

import strikd.Server;
import strikd.cluster.ServerDescriptor;
import strikd.communication.Opcodes;
import strikd.communication.incoming.MessageHandlers;
import strikd.communication.outgoing.NameChangedMessage;
import strikd.communication.outgoing.ServerCryptoMessage;
import strikd.communication.outgoing.SessionInfoMessage;
import strikd.communication.outgoing.VersionCheckMessage;
import strikd.game.match.Match;
import strikd.game.match.MatchPlayer;
import strikd.game.match.queues.PlayerQueue;
import strikd.game.user.User;
import strikd.net.NetConnection;
import strikd.net.codec.IncomingMessage;
import strikd.net.codec.OutgoingMessage;

public class Session extends Server.Referent
{
	private static final boolean USE_CRYPTO = true;
	
	private static final Logger logger = Logger.getLogger(Session.class);
	
	private final long sessionId;
	private final NetConnection connection;
	private boolean isEnded;
	
	private boolean handshakeOK;
	private User user;
	private MatchPlayer matchPlayer;
	private PlayerQueue.Entry queueEntry;
	
	public Session(long sessionId, NetConnection connection, Server server)
	{
		super(server);
		this.sessionId = sessionId;
		this.connection = connection;
	}

	public void hello()
	{
		// Forces client to validate version and update if needed
		this.send(new VersionCheckMessage(1, 0, "Waterduck"));
		
		// Crypto enabled?
		if(USE_CRYPTO)
		{
			// Initiate handshake flow
			byte[] key = UUID.randomUUID().toString().getBytes();
			this.send(new ServerCryptoMessage(key));
			this.connection.setServerCrypto(key);
		}
		else
		{
			// Short-circuit flow
			this.handshakeOK();
		}
	}
	
	public void handshakeOK()
	{
		// Handshake OK!
		logger.debug("handshake OK");
		this.handshakeOK = true;
		
		// Send session info
		ServerDescriptor server = this.getServer().getServerCluster().getSelf();
		this.send(new SessionInfoMessage(this.sessionId, server.name));
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
	
	public void onNetMessage(IncomingMessage msg)
	{
		logger.debug("received " + msg);
		
		if(this.handshakeOK || (msg.op == Opcodes.Incoming.CLIENT_CRYPTO))
		{
			MessageHandlers.get(msg.op).handle(this, msg);
		}
		else
		{
			this.end(String.format("received %s before handshake", msg.op));
		}
	}
	
	public void send(OutgoingMessage msg)
	{
		logger.debug("send " + msg);
		
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
	
	public boolean isHandshakeOK()
	{
		return this.handshakeOK;
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
	
	public void saveData()
	{
		if(this.isLoggedIn())
		{
			this.getServer().getUserRegister().saveUser(this.user);
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
	
	public void renameUser(String name)
	{
		if(this.isLoggedIn())
		{
			this.user.name = name;
			this.send(new NameChangedMessage(name));
		}
	}
}
