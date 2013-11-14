package strikd.net.codec;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import strikd.communication.Opcodes;

public class MessageDecoder extends ByteToMessageDecoder
{
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) throws Exception
	{
		// Mark reader index
		buffer.markReaderIndex();

		// Read message length
		short length = buffer.readShort();

		// Valid length and all data arrived?
		if(length > 0 && buffer.isReadable(length))
		{
			// Parse opcode
			Opcodes.Incoming op = Opcodes.Incoming.valueOf(buffer.readByte());
			
			// Copy bytes to an IncomingMessage (heapbuffer) and export this as a decoded message
			out.add(new IncomingMessage(op, buffer.readBytes(length - 1)));
		}
		else
		{
			// Try again when there's more data
			buffer.resetReaderIndex();
		}
	}
}
