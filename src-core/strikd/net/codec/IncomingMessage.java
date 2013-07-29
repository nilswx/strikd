package strikd.net.codec;

import org.jboss.netty.buffer.ChannelBuffer;

import strikd.communication.Opcodes;

public abstract class IncomingMessage extends NetMessage<Opcodes.Incoming>
{
	protected IncomingMessage(Opcodes.Incoming incoming, ChannelBuffer buf)
	{
		super(incoming, buf);
		this.data.writeShort(0); // Length placeholder
		this.data.writeByte(op.ordinal());
	}
	
	public boolean readBool()
	{
		return (this.data.readByte() == 1);
	}
	
	public byte readByte()
	{
		return this.data.readByte();
	}

	public int readInt()
	{
		return this.data.readInt();
	}
	
	public long readLong()
	{
		return this.data.readLong();
	}
	
	public String readStr()
	{
		// Read specified amount of bytes
		short length = this.data.readShort();
		byte[] bytes = new byte[length];
		this.data.readBytes(bytes);
		
		// Return result
		return new String(bytes);
	}
}
