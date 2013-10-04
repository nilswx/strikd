package strikd.net.codec;

import java.nio.charset.Charset;

import org.jboss.netty.buffer.ChannelBuffer;

import com.google.common.base.Charsets;

public abstract class NetMessage<T extends Enum<?>>
{
	protected static final Charset UTF_8 = Charsets.UTF_8;
	
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
		String buf = this.buf.toString(UTF_8);
		for(int i = 0; i <= 13; i++)
		{
			buf = buf.replace(Character.toString((char)i), "[" + i + "]");
		}
		return String.format("%s [%s]", this.op.name(), buf);
	}
}
