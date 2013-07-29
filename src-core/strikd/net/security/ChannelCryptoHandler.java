package strikd.net.security;

import org.jboss.netty.channel.ChannelDownstreamHandler;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelUpstreamHandler;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.MessageEvent;

public class ChannelCryptoHandler implements ChannelUpstreamHandler, ChannelDownstreamHandler
{
	private final RC4 send;
	private final RC4 receive;
	
	public ChannelCryptoHandler(byte[] key)
	{
		this.send = new RC4(key);
		this.receive = new RC4(key);
	}

	@Override
	public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent ce) throws Exception
	{
		if(ce instanceof MessageEvent)
		{
			MessageEvent e = (MessageEvent)ce;
			Object decrypted = e.getMessage(); // TODO: decrypt buffer
			
		}
	}
	
	@Override
	public void handleDownstream(ChannelHandlerContext ctx, ChannelEvent ce) throws Exception
	{
		if(ce instanceof MessageEvent)
		{
			MessageEvent e = (MessageEvent)ce;
			Object encrypted = e.getMessage(); // TODO: encrypt buffer
			Channels.write(ctx, e.getFuture(), e.getMessage(), e.getRemoteAddress());
		}
	}
}
