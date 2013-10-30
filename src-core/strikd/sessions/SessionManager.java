package strikd.sessions;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;

import strikd.Server;
import strikd.game.facebook.PersonBeatedStory;
import strikd.game.player.Player;
import strikd.net.NetConnection;

public class SessionManager extends Server.Referent
{
	private static final Logger logger = Logger.getLogger(SessionManager.class);
	
	private final AtomicLong sessionCounter = new AtomicLong();
	private final Map<Long, Session> sessions = new ConcurrentHashMap<Long, Session>();
	
	private final AtomicLong loginCounter = new AtomicLong();
	private final Map<ObjectId, Session> playerSessions = new ConcurrentHashMap<ObjectId, Session>();
	
	public SessionManager(Server server)
	{
		super(server);
	}

	/**
	 * Called once when a connection has performed crypto handshake.
	 * @param connection
	 * @return
	 */
	public Session newSession(NetConnection connection)
	{
		// Create a session with a new ID
		long sessionId = this.sessionCounter.incrementAndGet();
		Session session = new Session(sessionId, connection, this.getServer());
		connection.setSession(session);
		
		// Add to session map
		this.sessions.put(sessionId, session);
		logger.debug(String.format("session #%d connected from %s", session.getSessionId(), session.getConnection().getIpAddress()));
		
		// Greet session
		session.hello();
		
		return session;
	}
	
	public void endSession(long sessionId, String reason)
	{
		Session session = this.sessions.remove(sessionId);
		if(session != null)
		{
			Player player = session.getPlayer();
			if(player == null)
			{
				logger.warn(String.format("session #%d (%s) ended early (%s)", sessionId, session.getConnection().getIpAddress(), reason));
			}
			else
			{
				this.playerSessions.remove(player.id);
				logger.debug(String.format("%s logged out (%s)", player, reason));
			}
			session.onEnd();
		}
	}
	
	public void completeLogin(Session session)
	{
		// Given session is indeed logged in?
		Player player = session.getPlayer();
		if(player != null)
		{
			// Disconnect old login (if online)
			Session concurrent = this.getPlayerSession(player.id);
			if(concurrent != null)
			{
				this.endSession(concurrent.getSessionId(), "concurrent login");
			}
			
			// Add to player map and increment logins
			this.playerSessions.put(player.id, session);
			this.loginCounter.incrementAndGet();
			player.logins++;
			
			// Post story
			if(player.isFacebookLinked())
			{
				this.getServer().getFacebook().publish(new PersonBeatedStory(player.fbIdentity, "100000541030001"));
			}
			
			logger.info(String.format("%s logged in (#%d) in from %s (%s)", player, player.logins, session.getConnection().getIpAddress(), player.platform));
		}
	}
	
	public Session getSession(long sessionId)
	{
		return this.sessions.get(sessionId);
	}
	
	public Session getPlayerSession(ObjectId playerId)
	{
		return this.playerSessions.get(playerId);
	}
	
	public int sessions()
	{
		return this.sessions.size();
	}
	
	public long totalSessions()
	{
		return this.sessionCounter.get();
	}
	
	public int players()
	{
		return this.playerSessions.size();
	}
	
	public long totalLogins()
	{
		return this.loginCounter.get();
	}
}
