package strikd.net.security;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

public class ChannelEncryptionHandler extends ChannelCryptoHandler
{
	public ChannelEncryptionHandler(byte[] key)
	{
		super(key);
	}

	@Override
	public void writeRequested(ChannelHandlerContext ctx, MessageEvent e) throws Exception
	{
		if(e.getMessage() instanceof ChannelBuffer)
		{
			ChannelBuffer buffer = (ChannelBuffer)e.getMessage();
			this.crypto.cipherDirect(buffer.array(), buffer.arrayOffset(), buffer.readableBytes());
		}
		super.writeRequested(ctx, e);
	}
}
