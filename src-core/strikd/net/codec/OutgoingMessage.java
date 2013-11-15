package strikd.net.codec;

import java.util.Date;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import strikd.communication.Opcodes;

public abstract class OutgoingMessage extends NetMessage<Opcodes.Outgoing>
{
	protected OutgoingMessage(Opcodes.Outgoing op)
	{
		 // TODO: investigate pooled (and direct) buffers
		super(op, Unpooled.buffer(MessageAllocatorExpert.getBestSize(op)));
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
	
	public final void writeLong(Date d)
	{
		this.writeLong(d != null ? d.getTime() : 0);
	}
	
	public final void writeStr(Object o)
	{
		this.writeStr(o != null ? o.toString() : "");
	}
	
	@Override
	public final int length()
	{
		// The bytes of the int16 for message length are not counted, mister!
		return this.buf.writerIndex() - 2;
	}
	
	public ByteBuf getBuffer()
	{
		this.buf.setShort(0, this.length());
		
		MessageAllocatorExpert.reportSize(super.op, super.buf.readableBytes());
		
		return this.buf;
	}
}
