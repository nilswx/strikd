package strikd.net;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

import strikd.sessions.Session;
import strikd.sessions.SessionManager;

public class ConnectionManagementHandler extends SimpleChannelHandler
{
	private final SessionManager sessionMgr;
	
	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception
	{
		// Newly accepted channels become NetConnections, which will form the root of a Session after crypto
		NetConnection conn = new NetConnection(ctx.getChannel(), this.sessionMgr);
		
		// DEBUG: skip to post-handshake
		conn.initSession();
		Session session = conn.getSession();
		
		// DEBUG: skip to post-login
		this.sessionMgr.completeLogin(sessionMgr.newSession(conn));
	}
	
	private ConnectionManagementHandler(SessionManager sessionMgr)
	{
		this.sessionMgr = sessionMgr;
	}
	
	public static ConnectionManagementHandler getInstance(SessionManager sessionMgr)
	{
		return new ConnectionManagementHandler(sessionMgr);
	}
}
