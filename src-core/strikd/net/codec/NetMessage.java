package strikd.net.codec;

import java.nio.charset.Charset;

import org.jboss.netty.buffer.ChannelBuffer;

public abstract class NetMessage<T extends Enum<?>>
{
	public final T op;
	protected final ChannelBuffer buf;
	
	protected NetMessage(T op, ChannelBuffer data)
	{
		this.op = op;
		this.buf = data;
	}
	
	protected final String name()
	{
		return this.op.toString();
	}
	
	public abstract int length();
	
	@Override
	public final String toString()
	{
		String buf = this.buf.toString(Charset.defaultCharset());
		for(int i = 0; i <= 13; i++)
		{
			buf = buf.replace(Character.toString((char)i), "[" + i + "]");
		}
		return String.format("%s=0x%02X [%s]", this.op.name(), this.op.ordinal() & 0x0FFFFF, buf);
	}
}
