package strikd.net.codec;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

import strikd.communication.Opcodes;

public class MessageDecoder extends FrameDecoder
{
	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception
	{
		// Mark reader index
		buffer.markReaderIndex();
		
		// Read message length
		short length = buffer.readShort();
		
		// Valid length and all data arrived?
		if(length > 0 && buffer.readableBytes() >= length)
		{
			Opcodes.Incoming op = Opcodes.Incoming.valueOf(buffer.readByte());
			return new IncomingMessage(op, buffer.readBytes(length - 1)); // -1 because opcode byte already read
		}
		else
		{
			// Try again when there's more data
			buffer.resetReaderIndex();
			return null;
		}
	}
}
