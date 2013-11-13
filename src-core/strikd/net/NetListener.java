package strikd.net;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

import strikd.sessions.SessionManager;

public class NetListener
{
	private Channel listener;
	
	public NetListener(int port, final SessionManager sessionMgr) throws IOException
	{
		// Setup a server socket
		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.channel(NioServerSocketChannel.class);
		
		// Configure event loop
		bootstrap.group(new NioEventLoopGroup(), new NioEventLoopGroup());
		
		// Handle new connections
		bootstrap.childHandler(new ChannelInitializer<SocketChannel>()
		{
			private final ConnectionManagementHandler handler = ConnectionManagementHandler.getInstance(sessionMgr);
			
			@Override
			protected void initChannel(SocketChannel ch) throws Exception
			{
				ch.pipeline().addLast(this.handler);
			}
		});
		
		// Options
		bootstrap.childOption(ChannelOption.TCP_NODELAY, true);
		
		// Wait for listener to bind
		try
		{
			this.listener = bootstrap.bind(new InetSocketAddress(port)).syncUninterruptibly().channel();
		}
		catch(ChannelException ex)
		{
			throw new IOException(String.format("could not bind to tcp/%d", port), ex);
		}
	}
	
	public SocketAddress getLocalAddress()
	{
		return this.listener.localAddress();
	}
	
	// Disable Netty's thread renaming, create executors
	static
	{
		//ThreadRenamingRunnable.setThreadNameDeterminer(ThreadNameDeterminer.CURRENT);
	}
}
