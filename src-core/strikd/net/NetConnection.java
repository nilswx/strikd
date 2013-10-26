package strikd.net;

import java.net.InetSocketAddress;

import org.apache.log4j.Logger;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

import strikd.net.codec.IncomingMessage;
import strikd.net.codec.MessageDecoder;
import strikd.net.codec.MessageEncoder;
import strikd.net.codec.OutgoingMessage;
import strikd.net.security.ChannelDecryptionHandler;
import strikd.net.security.ChannelEncryptionHandler;
import strikd.sessions.Session;

public class NetConnection extends SimpleChannelHandler
{
	private static final Logger logger = Logger.getLogger(NetConnection.class);
	
	private final Channel channel;
	private final String ipAddress;
	private final long startTime;
	
	private Session session;
	private boolean closeRequested;
	
	public NetConnection(Channel channel)
	{
		this.channel = channel;
		
		this.ipAddress = ((InetSocketAddress)channel.getRemoteAddress()).getAddress().getHostAddress();
		this.startTime = System.currentTimeMillis();
		
		this.channel.getPipeline().addFirst("encoder", new MessageEncoder());
		this.channel.getPipeline().addFirst("decoder", new MessageDecoder());
		this.channel.getPipeline().addLast("connection", this);
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
		if(!this.closeRequested)
		{
			this.closeRequested = true;
			this.close();
			this.session.onNetClose(reason);
		}
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
		if(e.getMessage() instanceof IncomingMessage)
		{
			this.session.onNetMessage((IncomingMessage) e.getMessage());
		}
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
	{
		logger.error("caught exception", e.getCause());
		this.requestClose("disconnected by error");
	}
	
	public void send(OutgoingMessage msg)
	{
		if(this.channel.isOpen())
		{
			this.channel.write(msg);
		}
	}
	
	public void setServerCrypto(byte[] key)
	{
		this.channel.getPipeline().addBefore("encoder", "encryption", new ChannelEncryptionHandler(key));
	}
	
	public void setClientCrypto(byte[] key)
	{
		this.channel.getPipeline().addBefore("decoder", "decryption", new ChannelDecryptionHandler(key));
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
	
	public Session getSession()
	{
		return this.session;
	}

	public void setSession(Session session)
	{
		if(this.session == null)
		{
			this.session = session;
		}
	}
	
	public boolean isSecure()
	{
		ChannelPipeline pipeline = this.channel.getPipeline();
		return (pipeline.get(ChannelEncryptionHandler.class) != null && pipeline.get(ChannelDecryptionHandler.class) != null);
	}
}
