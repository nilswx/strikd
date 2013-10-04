package strikd.net.security;

import org.jboss.netty.channel.SimpleChannelHandler;

public abstract class ChannelCryptoHandler extends SimpleChannelHandler
{
	protected final FastRC4 crypto;
	
	protected ChannelCryptoHandler(byte[] key)
	{
		this.crypto = new FastRC4(key);
	}
}
