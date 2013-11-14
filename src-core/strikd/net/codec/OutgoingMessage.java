package strikd.net.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import strikd.communication.Opcodes;

public abstract class OutgoingMessage extends NetMessage<Opcodes.Outgoing>
{
	private static int BUFFER_ALLOC_BYTES = 64;
	
	protected OutgoingMessage(Opcodes.Outgoing op)
	{
		super(op, Unpooled.buffer(BUFFER_ALLOC_BYTES)); // TODO: investigate pooled (and direct) buffers
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
	
	public ByteBuf getBuffer()
	{
		this.buf.setShort(0, this.length());
		adjustBufferAllocator(this.buf.readableBytes());
		
		return this.buf;
	}
	
	private static final void adjustBufferAllocator(final int length)
	{
		// Message is bigger than ever seen before?
		if(length > BUFFER_ALLOC_BYTES)
		{
			// Future messages will have this capacity to avoid resizing buffers (RAM = cheap, CPU = not)
			BUFFER_ALLOC_BYTES = length;
		}
	}
}
