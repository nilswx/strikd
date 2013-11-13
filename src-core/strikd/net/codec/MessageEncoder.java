package strikd.net.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class MessageEncoder extends MessageToByteEncoder<OutgoingMessage>
{
	@Override
	protected void encode(ChannelHandlerContext ctx, OutgoingMessage msg, ByteBuf out) throws Exception
	{
		out.writeBytes(msg.getBuffer());
	}
}
