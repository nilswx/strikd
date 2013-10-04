package strikd.net.security;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

public class ChannelDecryptionHandler extends ChannelCryptoHandler
{
	public ChannelDecryptionHandler(byte[] key)
	{
		super(key);
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception
	{
		if(e.getMessage() instanceof ChannelBuffer)
		{
			ChannelBuffer buffer = (ChannelBuffer)e.getMessage();
			super.crypto.cipherDirect(buffer.array(), buffer.arrayOffset(), buffer.readableBytes());
		}
		super.messageReceived(ctx, e);
	}
}
