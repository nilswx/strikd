package strikd.net.codec;

import java.nio.charset.Charset;

import org.jboss.netty.buffer.ChannelBuffer;

public abstract class NetMessage<T extends Enum<?>>
{
	public final T op;
	protected final ChannelBuffer data;
	
	protected NetMessage(T op, ChannelBuffer data)
	{
		this.op = op;
		this.data = data;
	}
	
	protected final String getName()
	{
		return this.op.toString();
	}
	
	@Override
	public final String toString()
	{
		String buf = this.data.toString(Charset.defaultCharset());
		for(int i = 0; i <= 13; i++)
		{
			buf = buf.replace(Character.toString((char)i), "[" + i + "]");
		}
		return String.format("%s=%s [%s]", this.op.name(), this.getName(), buf);
	}
}
