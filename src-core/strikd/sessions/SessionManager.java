package strikd.sessions;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import strikd.net.NetConnection;

public class SessionManager
{
	private final Map<Long, Session> sessions = new ConcurrentHashMap<Long, Session>();
	
	public void newSession(NetConnection connection)
	{
		// called when a connection has performed crypto handshake
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
