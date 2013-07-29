package strikd.net.codec;

import org.jboss.netty.buffer.ChannelBuffers;

import strikd.communication.Opcodes;

public abstract class OutgoingMessage extends NetMessage<Opcodes.Outgoing>
{
	protected OutgoingMessage(Opcodes.Outgoing op)
	{
		super(op, ChannelBuffers.dynamicBuffer());
		this.buf.writeShort(0); // Length placeholder
		this.buf.writeByte(op.ordinal());
	}
	
	public final void writeBool(boolean b)
	{
		this.buf.writeByte(b ? 1 : 0);
	}
	
	public final void writeByte(byte b)
	{
		this.buf.writeByte(b);
	}
	
	public final void writeInt(int i)
	{
		this.buf.writeInt(i);
	}
	
	public final void writeLong(long i)
	{
		this.buf.writeLong(i);
	}
	
	public final void writeStr(String str)
	{
		if(str == null) str = "";
		byte[] bytes = str.getBytes();
		this.buf.writeShort(bytes.length);
		this.buf.writeBytes(bytes);
	}
	
	@Override
	public final int length()
	{
		// The bytes of the int16 for message length are not counted, mister!
		return this.buf.writerIndex() - 2;
	}
}
