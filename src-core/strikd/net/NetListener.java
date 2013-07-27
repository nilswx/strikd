package strikd.net;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.util.ThreadNameDeterminer;
import org.jboss.netty.util.ThreadRenamingRunnable;

import strikd.net.codec.StrikMessage;

public class NetListener
{
	private static final Logger logger = Logger.getLogger(NetListener.class);
	
	private ServerBootstrap server;
	private ExecutorService bossExecutor;
	private ExecutorService workerExecutor;
	
	public NetListener(int port)
	{
		// Create thread pools
		this.bossExecutor = Executors.newCachedThreadPool(new NamedThreadFactory("NetServer/Boss #%d"));
		this.workerExecutor = Executors.newCachedThreadPool(new NamedThreadFactory("NetServer/Worker #%d"));
		
		// Create server boss and worker threadpools using defaults
		this.server = new ServerBootstrap(new NioServerSocketChannelFactory(this.bossExecutor, this.workerExecutor));
		
		// Add a 'connection acceptor' handler to the pipeline
		this.server.setPipeline(Channels.pipeline(new SimpleChannelHandler()
		{
			@Override
			public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception
			{
				NetConnection conn = new NetConnection(ctx.getChannel());
				logger.info("accepted conn from " + conn.getIpAddress());
				
				
				
				StrikMessage msg = new StrikMessage("WELCOME");
				msg.set("motd", "Welcome to the server!");
				msg.set("sessId", 4488448);
				conn.send(msg);
				
				StrikMessage msg2 = new StrikMessage("ALERT");
				msg2.set("text", "Strik is in beta!");
				msg2.set("color", "blue");
				conn.send(msg2);
				
				for(int i = 0; i < 50; i++)
				{
					conn.send(msg);
					conn.send(msg2);
				}
				
			}
			
			@Override
			public String toString()
			{
				return "NetConnectionAcceptor";
			}
		}));
		
		// Setup pipeline factor
		Channel listener = this.server.bind(new InetSocketAddress(port));
		if(listener.isOpen())
		{
			logger.info(String.format("listening on tcp/%d", port));
		}
	}
	
	static
	{
		// Disable Netty's thread renaming, create executors
		ThreadRenamingRunnable.setThreadNameDeterminer(ThreadNameDeterminer.CURRENT);
	}
}
