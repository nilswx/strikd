package strikd.net.codec;

import org.jboss.netty.buffer.ChannelBuffer;

import strikd.communication.Opcodes;

public final class IncomingMessage extends NetMessage<Opcodes.Incoming>
{
	protected IncomingMessage(Opcodes.Incoming op, ChannelBuffer buf)
	{
		super(op, buf);
	}
	
	public final boolean readBool()
	{
		return (this.buf.readByte() == 1);
	}
	
	public final byte readByte()
	{
		return this.buf.readByte();
	}

	public final int readInt()
	{
		return this.buf.readInt();
	}
	
	public final long readLong()
	{
		return this.buf.readLong();
	}
	
	public final String readStr()
	{
		short length = this.buf.readShort();
		byte[] bytes = new byte[length];
		this.buf.readBytes(bytes);
		
		return new String(bytes, UTF_8);
	}
	
	public final int length()
	{
		// The bytes of the int16 for message length are not counted, mister!
		return this.buf.readableBytes() - 2;
	}
}
