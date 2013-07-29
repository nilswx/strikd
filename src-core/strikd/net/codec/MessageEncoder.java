package strikd.net.codec;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

public class MessageEncoder extends OneToOneEncoder
{
	@Override
	protected Object encode(ChannelHandlerContext ctx, Channel channel, Object obj) throws Exception
	{
		if(obj instanceof OutgoingMessage)
		{
			return null;
		}
		else
		{
			return obj;
		}
	}
}
