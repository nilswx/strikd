package strikd.net.security;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ChannelDecryptionHandler extends ChannelInboundHandlerAdapter
{
	private final FastRC4 crypto;
	
	public ChannelDecryptionHandler(byte[] key)
	{
		this.crypto = new FastRC4(key);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
	{
		if(msg instanceof ByteBuf)
		{
			ByteBuf buffer = (ByteBuf)msg;
			this.crypto.cipherBuffer(buffer, buffer.readerIndex(), buffer.readableBytes());
		}
		super.channelRead(ctx, msg);
	}
}
