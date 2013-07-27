package strikd.net.codec;

import java.util.HashMap;
import java.util.Map;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBufferInputStream;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;
import org.msgpack.MessagePack;
import org.msgpack.unpacker.MessagePackUnpacker;
import org.msgpack.unpacker.Unpacker;

public class MessageDecoder extends FrameDecoder
{
	private final MessagePack pak = new MessagePack();
	
	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception
	{
		Unpacker unpak = new MessagePackUnpacker(this.pak, new ChannelBufferInputStream(buffer));
		String op = unpak.readString();
		Map<String, Object> data = unpak.read(HashMap.class);
		
		return new StrikMessage(op, data);
	}
}
