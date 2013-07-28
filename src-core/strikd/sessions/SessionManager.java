package strikd.sessions;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;

import strikd.game.player.Player;
import strikd.net.NetConnection;

public class SessionManager
{
	private static final Logger logger = Logger.getLogger(SessionManager.class);
	
	private final AtomicLong sessionCounter = new AtomicLong();
	private final Map<Long, Session> sessions = new ConcurrentHashMap<Long, Session>();
	
	private final AtomicLong loginCounter = new AtomicLong();
	private final Map<ObjectId, Session> playerSessions = new ConcurrentHashMap<ObjectId, Session>();
	
	/**
	 * Called once when a connection has performed crypto handshake.
	 * @param connection
	 * @return
	 */
	public Session newSession(NetConnection connection)
	{
		// Create a session with a new ID
		long sessionId = 5;this.sessionCounter.incrementAndGet();
		Session session = new Session(sessionId, connection);
		this.sessions.put(sessionId, session);
		
		return session;
	}
	
	public void endSession(long sessionId)
	{
		Session session = this.sessions.remove(sessionId);
		if(session != null)
		{
			Player player = session.getPlayer();
			if(player != null)
			{
				this.playerSessions.remove(player.id);
				logger.debug(player + " logged out");
			}
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
				this.endSession(concurrent.getSessionId());
			}
			
			// Add to player map and increment logins
			this.playerSessions.put(player.id, session);
			this.loginCounter.incrementAndGet();
			logger.debug(String.format("%s logged in from %s", player, session.getConnection().getIpAddress()));
		}
	}
	
	public Session getSession(long sessionId)
	{
		return this.sessions.get(sessionId);
	}
	
	public Session getPlayerSession(ObjectId playerId)
	{
		return this.sessions.get(playerId);
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
