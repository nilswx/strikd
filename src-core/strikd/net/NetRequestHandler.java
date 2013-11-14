package strikd.net;

import strikd.net.codec.IncomingMessage;
import strikd.sessions.Session;
import strikd.util.NamedThreadFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

public class NetRequestHandler extends ChannelInboundHandlerAdapter
{
	private static final int NUM_HANDLER_THREADS = Runtime.getRuntime().availableProcessors();
	
	private final Session session;
	
	private NetRequestHandler(Session session)
	{
		this.session = session;
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
	{
		if(msg instanceof IncomingMessage)
		{
			this.session.onNetMessage((IncomingMessage)msg);
		}
	}
	
	private static final EventExecutorGroup eventExecutorGroup = new DefaultEventExecutorGroup(NUM_HANDLER_THREADS, new NamedThreadFactory("Network/RequestHandler #%d")); 
	
	public static final EventExecutorGroup getEventExecutorGroup()
	{
		return eventExecutorGroup;
	}
	
	public static NetRequestHandler getRequestHandler(Session session)
	{
		return new NetRequestHandler(session);
	}
}
