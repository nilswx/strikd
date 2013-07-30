package strikd.net.codec;

import org.jboss.netty.buffer.ChannelBuffer;
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
			OutgoingMessage msg = (OutgoingMessage)obj;
			
			ChannelBuffer buf = msg.getBuffer();
			
			return buf;
		}
		else
		{
			return obj;
		}
	}
}
