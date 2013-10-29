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
		StringBuilder sb = new StringBuilder();
		
		// Message type
		sb.append((this instanceof IncomingMessage) ? '<' : '>');
		sb.append(' ');
		sb.append(this.op.name());
		sb.append(' ');
		
		// Buffer wrapped between [ ]
		sb.append('[');
		byte[] array = this.buf.array();
		for(int i = this.buf.arrayOffset(); i < this.length() + 2; i++)
		{
			// Printable?
			if(array[i] > 31 && array[i] < 127)
			{
				sb.append((char)array[i]);
			}
			else
			{
				sb.append('[');
				sb.append(array[i]);
				sb.append(']');
			}
		}
		sb.append(']');
		
		return sb.toString();
	}
}
