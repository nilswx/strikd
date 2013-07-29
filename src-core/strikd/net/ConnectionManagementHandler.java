package strikd.net;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

import strikd.net.codec.StrikMessage;
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
		
		// Greet client
		StrikMessage msg = new StrikMessage("WELCOME");
		msg.set("sessId", session.getSessionId());
		//msg.set("instance", instance.getDescriptor().name);
		//msg.set("version", instance.getDescriptor().version());
		//msg.set("avgWaitingTime", instance.getDescriptor().avgWaitingTime());
		msg.set("motd", "Win 4 games today to collect the special XMAS 2013 Hat!");
		session.send(msg);
		
		// DEBUG: skip to post-login
		this.sessionMgr.completeLogin(sessionMgr.newSession(conn));
		
		// Login OK!
		StrikMessage msg2 = new StrikMessage("LOGIN_OK");
		msg2.set("playerId", session.getPlayer().id.toString());
		msg2.set("name", session.getPlayer().name);
		session.send(msg2);
		
		// Sure!
		StrikMessage msg3 = new StrikMessage("ALERT");
		msg3.set("text", "Strik is in beta!");
		msg3.set("color", "blue");
		session.send(msg3);
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
