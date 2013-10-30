package strikd.sessions;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;

import strikd.Server;
import strikd.game.facebook.PersonBeatedStory;
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
			User user = session.getUser();
			if(user == null)
			{
				logger.warn(String.format("session #%d (%s) ended early (%s)", sessionId, session.getConnection().getIpAddress(), reason));
			}
			else
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
			
			// Post story
			if(user.isFacebookLinked())
			{
				this.getServer().getFacebook().publish(new PersonBeatedStory(user.fbIdentity, "100000541030001"));
			}
			
			logger.info(String.format("%s logged in (#%d) in from %s (%s)", user, user.logins, session.getConnection().getIpAddress(), user.platform));
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
