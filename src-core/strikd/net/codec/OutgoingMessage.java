package strikd.net.codec;

import org.jboss.netty.buffer.ChannelBuffers;

import strikd.communication.Opcodes;

public abstract class OutgoingMessage extends NetMessage<Opcodes.Outgoing>
{
	protected OutgoingMessage(Opcodes.Outgoing op)
	{
		super(op, ChannelBuffers.dynamicBuffer());
		this.data.writeShort(0); // Length placeholder
		this.data.writeByte(op.ordinal());
	}
	
	public void writeBool(boolean b)
	{
		this.data.writeByte(b ? 1 : 0);
	}
	
	public void writeInt(int i)
	{
		this.data.writeInt(i);
	}
	
	public void writeStr(String str)
	{
		if(str == null) str = "";
		byte[] bytes = str.getBytes();
		this.data.writeShort(bytes.length);
		this.data.writeBytes(bytes);
	}
	
	public int length()
	{
		// The bytes of the int16 for message length are not counted, mister!
		return this.data.writerIndex() - 2;
	}
}
