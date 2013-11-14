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
import strikd.util.NamedThreadFactory;

public class NetServer
{
	private final ServerBootstrap bootstrap;
	private final Channel listener;
	
	public NetServer(int port, final SessionManager sessionMgr) throws IOException
	{
		// Setup a server socket
		this.bootstrap = new ServerBootstrap();
		this.bootstrap.channel(NioServerSocketChannel.class);
		
		// Configure event loop thread pools
		this.bootstrap.group(new NioEventLoopGroup(0, new NamedThreadFactory("Network/Listener")),
						new NioEventLoopGroup(0, new NamedThreadFactory("Network/IO #%d")));
		
		// Handle new connections to become sessions in the session manager
		this.bootstrap.childHandler(new ChannelInitializer<SocketChannel>()
		{
			@Override
			protected void initChannel(SocketChannel channel) throws Exception
			{
				sessionMgr.newSession(new NetConnection(channel));
			}
		});
		
		// Options
		this.bootstrap.childOption(ChannelOption.TCP_NODELAY, true);
		
		// Wait for listener to bind
		try
		{
			this.listener = this.bootstrap.bind(new InetSocketAddress(port)).syncUninterruptibly().channel();
		}
		catch(ChannelException ex)
		{
			throw new IOException(String.format("could not bind to tcp/%d", port), ex);
		}
	}
	
	public void shutdown()
	{
	}
	
	public SocketAddress getLocalAddress()
	{
		return this.listener.localAddress();
	}
}
