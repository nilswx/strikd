package strikd.net;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import strikd.sessions.SessionManager;

@ChannelInboundHandlerAdapter.Sharable
public class ConnectionManagementHandler extends ChannelInboundHandlerAdapter
{
	private final SessionManager sessionMgr;
	
	@Override
	public void channelActive(ChannelHandlerContext ctx)
	{
		NetConnection conn = new NetConnection(ctx.channel());
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
