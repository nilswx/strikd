package strikd.net;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
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
	private ServerBootstrap bootstrap;
	private Channel listener;
	
	public NetServer(int port, SessionManager sessionMgr) throws IOException
	{
		// Setup a server socket
		this.bootstrap = new ServerBootstrap();
		this.bootstrap.channel(NioServerSocketChannel.class);
		
		// Configure event loop thread pools
		this.bootstrap.group(new NioEventLoopGroup(0, new NamedThreadFactory("Network/Listener")),
						new NioEventLoopGroup(0, new NamedThreadFactory("Network/IO #%d")));
		
		// Handle new connections to become sessions in the session manager
		final NewConnectionHandler handler = new NewConnectionHandler(sessionMgr);
		this.bootstrap.childHandler(new ChannelInitializer<SocketChannel>()
		{
			@Override
			protected void initChannel(SocketChannel ch) throws Exception
			{
				ch.pipeline().addLast(handler);
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
	
	@Sharable
	private class NewConnectionHandler extends ChannelInboundHandlerAdapter
	{
		private final SessionManager sessionMgr;
		
		public NewConnectionHandler(SessionManager sessionMgr)
		{
			this.sessionMgr = sessionMgr;
		}

		@Override
		public void channelActive(ChannelHandlerContext ctx)
		{
			NetConnection conn = new NetConnection(ctx.channel());
			this.sessionMgr.newSession(conn);
		}
	}
}
