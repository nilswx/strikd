package strikd.net.codec;

import org.msgpack.MessagePack;
import org.msgpack.packer.Packer;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBufferOutputStream;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

public class MessageEncoder extends OneToOneEncoder
{
	private final ChannelBuffer buff = ChannelBuffers.dynamicBuffer();
	private final Packer pak = new MessagePack().createPacker(new ChannelBufferOutputStream(this.buff));
	
	@Override
	protected Object encode(ChannelHandlerContext ctx, Channel channel, Object obj) throws Exception
	{
		if(obj instanceof StrikMessage)
		{
			this.reset();
			
			StrikMessage msg = (StrikMessage) obj;
			this.pak.writeArrayBegin(2);
			this.pak.write(msg.getOp());
			this.pak.write(msg.getData());
			
			return this.buff;
		}
		else
		{
			return obj;
		}
	}
	
	private void reset()
	{
		this.buff.resetReaderIndex();
		this.buff.resetWriterIndex();
	}
}
