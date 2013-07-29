package strikd.net.codec;

import org.jboss.netty.buffer.ChannelBuffer;

import strikd.communication.Opcodes;

public abstract class IncomingMessage extends NetMessage<Opcodes.Incoming>
{
	protected IncomingMessage(Opcodes.Incoming incoming, ChannelBuffer buf)
	{
		super(incoming, buf);
		this.buf.writeShort(0); // Length placeholder
		this.buf.writeByte(op.ordinal());
	}
	
	public boolean readBool()
	{
		return (this.buf.readByte() == 1);
	}
	
	public byte readByte()
	{
		return this.buf.readByte();
	}

	public int readInt()
	{
		return this.buf.readInt();
	}
	
	public long readLong()
	{
		return this.buf.readLong();
	}
	
	public String readStr()
	{
		// Read specified amount of bytes
		short length = this.buf.readShort();
		byte[] bytes = new byte[length];
		this.buf.readBytes(bytes);
		
		// Return result
		return new String(bytes);
	}
	
	public final int length()
	{
		return 0;
	}
}
