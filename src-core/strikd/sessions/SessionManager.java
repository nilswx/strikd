package strikd.sessions;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import strikd.net.NetConnection;

public class SessionManager
{
	private final AtomicLong sessionCounter = new AtomicLong();
	private final Map<Long, Session> sessions = new ConcurrentHashMap<Long, Session>();
	
	/**
	 * Called once when a connection has performed crypto handshake.
	 * @param connection
	 * @return
	 */
	public Session newSession(NetConnection connection)
	{
		// Create a session with a new ID
		long sessionId = sessionCounter.incrementAndGet();
		Session session = new Session(sessionId, connection);
		this.sessions.put(sessionId, session);
		
		return session;
	}
	
	public Session getSession(long sessionId)
	{
		return this.sessions.get(sessionId);
	}
	
	public int sessions()
	{
		return this.sessions.size();
	}
}
