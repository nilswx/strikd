package strikd.net;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

import strikd.sessions.SessionManager;

public class ConnectionManagementHandler extends SimpleChannelHandler
{
	private final SessionManager sessionMgr;
	
	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception
	{
		NetConnection conn = new NetConnection(ctx.getChannel());
		this.sessionMgr.newSession(conn);
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
