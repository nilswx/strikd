package strikd.net.security;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

public class ChannelEncryptionHandler extends ChannelOutboundHandlerAdapter
{
	private final FastRC4 crypto;
	
	public ChannelEncryptionHandler(byte[] key)
	{
		this.crypto = new FastRC4(key);
	}

	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception
	{
		if(msg instanceof ByteBuf)
		{
			ByteBuf buffer = (ByteBuf)msg;
			this.crypto.cipherDirect(buffer.array(), buffer.arrayOffset(), buffer.readableBytes());
		}
		super.write(ctx, msg, promise);
	}
}
