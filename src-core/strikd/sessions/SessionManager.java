package strikd.sessions;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import strikd.Server;
import strikd.game.player.Player;
import strikd.net.NetConnection;

public class SessionManager extends Server.Referent
{
	private static final Logger logger = LoggerFactory.getLogger(SessionManager.class);
	
	private final AtomicLong sessionCounter = new AtomicLong();
	private final Map<Long, Session> sessions = new ConcurrentHashMap<Long, Session>();
	
	private final AtomicLong loginCounter = new AtomicLong();
	private final Map<Integer, Session> playerSessions = new ConcurrentHashMap<Integer, Session>();
	
	public SessionManager(Server server)
	{
		super(server);
	}
	
	public Session newSession(NetConnection connection)
	{
		// Create a session with a new ID
		long sessionId = this.sessionCounter.incrementAndGet();
		Session session = new Session(sessionId, connection, this.getServer());
		connection.setSession(session);
		
		// Add to session map
		this.sessions.put(sessionId, session);
		logger.debug("session #{} connected from {}", session.getSessionId(), session.getConnection().getIpAddress());
		
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
				logger.warn("session #{} ({}) ended early ({})", sessionId, session.getConnection().getIpAddress(), reason);
			}
			else
			{
				// Set server
				player.setServerId(0);
				
				// Remove session
				this.playerSessions.remove(player.getId());
				logger.debug("{} logged out ({})", player, reason);
			}
			
			// Process destroy logic
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
			Session concurrent = this.getPlayerSession(player.getId());
			if(concurrent != null)
			{
				this.endSession(concurrent.getSessionId(), "concurrent login");
			}
			
			// Set server
			player.setLogins(player.getLogins() + 1);
			player.setServerId(this.getServer().getServerCluster().getSelf().getId());
			
			// Add to player map
			this.playerSessions.put(player.getId(), session);
			this.loginCounter.incrementAndGet();

			// Hello!
			logger.info("{} logged in (#{}) in from {} ({})", player, player.getLogins(), session.getConnection().getIpAddress(), player.getPlatform());
		}
	}
	
	public Session getSession(long sessionId)
	{
		return this.sessions.get(sessionId);
	}
	
	public Session getPlayerSession(int playerId)
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
