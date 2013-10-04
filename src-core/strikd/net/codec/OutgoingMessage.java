package strikd.net.codec;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import strikd.communication.Opcodes;

public abstract class OutgoingMessage extends NetMessage<Opcodes.Outgoing>
{
	protected OutgoingMessage(Opcodes.Outgoing op)
	{
		super(op, ChannelBuffers.dynamicBuffer());
		this.buf.writeShort(0); // Length placeholder
		this.buf.writeByte(op.ordinal()); // Opcode placeholder
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
		
		byte[] bytes = str.getBytes(UTF_8);
		this.buf.writeShort(bytes.length);
		this.buf.writeBytes(bytes);
	}
	
	@Override
	public final int length()
	{
		// The bytes of the int16 for message length are not counted, mister!
		return this.buf.writerIndex() - 2;
	}
	
	public ChannelBuffer getBuffer()
	{
		this.buf.setShort(0, this.length());
		return this.buf;
	}
}
