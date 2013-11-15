package strikd.net.codec;

import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;

import com.google.common.base.Charsets;

public abstract class NetMessage<T extends Enum<?>>
{
	protected static final Charset UTF_8 = Charsets.UTF_8;
	
	public final T op;
	protected final ByteBuf buf;
	
	protected NetMessage(T op, ByteBuf data)
	{
		this.op = op;
		this.buf = data;
	}
	
	public void retain()
	{
		this.buf.retain();
	}
	
	public void release()
	{
		this.buf.release();
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
		for(int i = (this.buf.readerIndex() + ((this instanceof OutgoingMessage) ? 2+1 : 0)); i < this.buf.readableBytes(); i++)
		{
			// Printable?
			byte b = this.buf.getByte(i);
			if(b > 31 && b < 127)
			{
				sb.append((char)b);
			}
			else
			{
				sb.append('[');
				sb.append(b);
				sb.append(']');
			}
		}
		sb.append(']');
		
		return sb.toString();
	}
}
