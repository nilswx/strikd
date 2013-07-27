package strikd.net;

import java.net.InetSocketAddress;

import org.apache.log4j.Logger;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

import strikd.net.codec.MessageEncoder;
import strikd.net.codec.StrikMessage;

public class NetConnection extends SimpleChannelHandler
{
	private static final Logger logger = Logger.getLogger(NetConnection.class);
	
	private final Channel channel;
	private final String ipAddress;
	private final long startTime;
	
	public NetConnection(Channel channel)
	{
		this.channel = channel;
		this.ipAddress = ((InetSocketAddress)channel.getRemoteAddress()).getAddress().getHostAddress();
		this.startTime = System.currentTimeMillis();
		this.channel.getPipeline().addLast("encoder", new MessageEncoder());
	}
	
	public void close()
	{
		if(this.channel.isOpen())
		{
			this.channel.write(ChannelBuffers.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
		}
	}

	private void requestClose(String reason)
	{
		this.close();
		
		// TODO: notify session with reason
		logger.info(reason);
	}
	
	@Override
	public void disconnectRequested(ChannelHandlerContext ctx, ChannelStateEvent e)
	{
		this.requestClose("disconnected by user");
	}
	
	@Override
	public void channelDisconnected(ChannelHandlerContext cxt, ChannelStateEvent e)
	{
		this.requestClose("disconnected by user");
	}
	
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
	{
		logger.debug("received " + e.getMessage());
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
	{
		this.requestClose("disconnected by error");
		logger.error("caught exception", e.getCause());
	}
	
	public void send(StrikMessage msg)
	{
		logger.debug(msg);
		this.channel.write(msg);
	}
	
	public boolean isOpen()
	{
		return this.channel.isOpen();
	}
	
	public String getIpAddress()
	{
		return this.ipAddress;
	}
	
	public long getUptime()
	{
		return (System.currentTimeMillis() - this.startTime);
	}
}
