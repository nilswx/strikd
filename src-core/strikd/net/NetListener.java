package strikd.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelException;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.util.ThreadNameDeterminer;
import org.jboss.netty.util.ThreadRenamingRunnable;

import strikd.sessions.SessionManager;
import strikd.util.NamedThreadFactory;

public class NetListener
{
	private Channel listener;
	private ExecutorService bossExecutor;
	private ExecutorService workerExecutor;
	
	public NetListener(int port, SessionManager sessionMgr) throws IOException
	{
		// Create thread pools
		this.bossExecutor = Executors.newCachedThreadPool(new NamedThreadFactory("Network/Boss #%d"));
		this.workerExecutor = Executors.newCachedThreadPool(new NamedThreadFactory("Network/Worker #%d"));
		
		// Create server boss and worker thread pools using defaults
		ServerBootstrap bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(this.bossExecutor, this.workerExecutor));
		
		// Add a 'connection management handler' to ALL accepted channels
		ConnectionManagementHandler mgmt = ConnectionManagementHandler.getInstance(sessionMgr);
		bootstrap.setPipeline(Channels.pipeline(mgmt));
		
		// Bind listener
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
