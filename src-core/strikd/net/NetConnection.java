package strikd.net;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;

import java.net.InetSocketAddress;

import org.apache.log4j.Logger;

import strikd.net.codec.IncomingMessage;
import strikd.net.codec.MessageDecoder;
import strikd.net.codec.OutgoingMessage;
import strikd.net.security.ChannelDecryptionHandler;
import strikd.net.security.ChannelEncryptionHandler;
import strikd.sessions.Session;

public class NetConnection extends ChannelInboundHandlerAdapter
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
		
		this.ipAddress = ((InetSocketAddress)channel.remoteAddress()).getAddress().getHostAddress();
		this.startTime = System.currentTimeMillis();
		
		this.channel.pipeline().addFirst("decoder", new MessageDecoder());
		this.channel.pipeline().addLast("connection", this);
	}
	
	public void close()
	{
		if(this.channel.isOpen())
		{
			this.channel.close();
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
	public void channelInactive(ChannelHandlerContext cxt)
	{
		this.requestClose("disconnected by user");
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
	{
		if(msg instanceof IncomingMessage)
		{
			this.session.onNetMessage((IncomingMessage)msg);
		}
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
	{
		logger.error("caught exception", cause);
		this.requestClose("disconnected by error");
	}
	
	public void send(OutgoingMessage msg)
	{
		if(this.channel.isOpen())
		{
			this.channel.writeAndFlush(msg.getBuffer()).addListener(new ChannelFutureListener()
			{
				@Override
				public void operationComplete(ChannelFuture future) throws Exception
				{
					System.out.println("DONE");
				}
			});
		}
	}
	
	public void setServerCrypto(byte[] key)
	{
		this.channel.pipeline().addBefore("encoder", "encryption", new ChannelEncryptionHandler(key));
	}
	
	public void setClientCrypto(byte[] key)
	{
		this.channel.pipeline().addBefore("decoder", "decryption", new ChannelDecryptionHandler(key));
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
		ChannelPipeline pipeline = this.channel.pipeline();
		return (pipeline.get(ChannelEncryptionHandler.class) != null && pipeline.get(ChannelDecryptionHandler.class) != null);
	}
}
