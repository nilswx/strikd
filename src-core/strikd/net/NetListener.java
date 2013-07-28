package strikd.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelException;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.util.ThreadNameDeterminer;
import org.jboss.netty.util.ThreadRenamingRunnable;

import strikd.ServerInstance;
import strikd.net.codec.StrikMessage;
import strikd.sessions.Session;
import strikd.sessions.SessionManager;

public class NetListener
{
	private Channel listener;
	private ExecutorService bossExecutor;
	private ExecutorService workerExecutor;
	
	public NetListener(int port, final ServerInstance instance) throws IOException
	{
		// Create thread pools
		this.bossExecutor = Executors.newCachedThreadPool(new NamedThreadFactory("NetServer/Boss #%d"));
		this.workerExecutor = Executors.newCachedThreadPool(new NamedThreadFactory("NetServer/Worker #%d"));
		
		// Create server boss and worker threadpools using defaults
		ServerBootstrap bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(this.bossExecutor, this.workerExecutor));
		
		// Get session manager
		final SessionManager sessionMgr = instance.getSessionMgr();
		
		// Add a 'connection acceptor' handler to the pipeline
		bootstrap.setPipeline(Channels.pipeline(new SimpleChannelHandler()
		{
			@Override
			public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception
			{
				NetConnection conn = new NetConnection(ctx.getChannel(), sessionMgr);
				
				// DEBUG: skip to post-handshake
				Session session = sessionMgr.newSession(conn);
				
				// Greet client
				StrikMessage msg = new StrikMessage("WELCOME");
				msg.set("sessId", session.getSessionId());
				msg.set("instance", instance.getDescriptor().name);
				msg.set("version", instance.getDescriptor().version());
				msg.set("avgWaitingTime", instance.getDescriptor().avgWaitingTime());
				msg.set("motd", "Win 4 games today to collect the special XMAS 2013 Hat!");
				session.send(msg);
				
				// DEBUG: skip to post-login
				sessionMgr.completeLogin(sessionMgr.newSession(conn));
				
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
			
			@Override
			public String toString()
			{
				return "NetConnectionAcceptor";
			}
		}));
		
		// Setup pipeline factor
		try
		{
			this.listener = bootstrap.bind(new InetSocketAddress(port));
		}
		catch(ChannelException ex)
		{
			throw new IOException(String.format("could not bind to tcp/%d", port), ex);
		}
		
	}
	
	public SocketAddress getLocalAddress()
	{
		return this.listener.getLocalAddress();
	}
	
	// Disable Netty's thread renaming, create executors
	static
	{
		ThreadRenamingRunnable.setThreadNameDeterminer(ThreadNameDeterminer.CURRENT);
	}
}
