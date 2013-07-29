package strikd.communication.outgoing;

import strikd.communication.Opcodes;
import strikd.net.codec.OutgoingMessage;

public class NopMessage extends OutgoingMessage
{
	public NopMessage()
	{
		super(Opcodes.Outgoing.NOP);
	}
}
