package strikd.sessions;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;

import strikd.Server;
import strikd.ServerDescriptor;
import strikd.communication.outgoing.SessionInfoMessage;
import strikd.communication.outgoing.VersionCheckMessage;
import strikd.game.user.User;
import strikd.net.NetConnection;

public class SessionManager extends Server.Referent
{
	private static final Logger logger = Logger.getLogger(SessionManager.class);
	
	private final AtomicLong sessionCounter = new AtomicLong();
	private final Map<Long, Session> sessions = new ConcurrentHashMap<Long, Session>();
	
	private final AtomicLong loginCounter = new AtomicLong();
	private final Map<ObjectId, Session> userSessions = new ConcurrentHashMap<ObjectId, Session>();
	
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
		this.sessions.put(sessionId, session);
		
		// Greet session
		ServerDescriptor server = this.getServer().getDescriptor();
		session.send(new SessionInfoMessage(sessionId, server.name));
		
		// Notify of latest version (forces client to validate and update if needed)
		session.send(new VersionCheckMessage(1, 0, "Waterduck"));
		
		return session;
	}
	
	public void endSession(long sessionId, String reason)
	{
		Session session = this.sessions.remove(sessionId);
		if(session != null)
		{
			User user = session.getUser();
			if(user != null)
			{
				this.userSessions.remove(user.id);
				logger.debug(String.format("%s logged out (%s)", user, reason));
			}
			session.onEnd();
		}
	}
	
	public void completeLogin(Session session)
	{
		// Given session is indeed logged in?
		User user = session.getUser();
		if(user != null)
		{
			// Disconnect old login (if online)
			Session concurrent = this.getUserSession(user.id);
			if(concurrent != null)
			{
				this.endSession(concurrent.getSessionId(), "concurrent login");
			}
			
			// Add to user map and increment logins
			this.userSessions.put(user.id, session);
			this.loginCounter.incrementAndGet();
			user.logins++;
			
			logger.debug(String.format("%s logged in (#%d) in from %s (%s)", user, user.logins, session.getConnection().getIpAddress(), user.platform));
		}
	}
	
	public Session getSession(long sessionId)
	{
		return this.sessions.get(sessionId);
	}
	
	public Session getUserSession(ObjectId userId)
	{
		return this.userSessions.get(userId);
	}
	
	public int sessions()
	{
		return this.sessions.size();
	}
	
	public long totalSessions()
	{
		return this.sessionCounter.get();
	}
	
	public int users()
	{
		return this.userSessions.size();
	}
	
	public long totalLogins()
	{
		return this.loginCounter.get();
	}
}
