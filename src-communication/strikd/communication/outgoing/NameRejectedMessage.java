package strikd.communication.outgoing;

import strikd.communication.Opcodes;
import strikd.net.codec.OutgoingMessage;

public class NameRejectedMessage extends OutgoingMessage
{
	public NameRejectedMessage(String name, String message)
	{
		super(Opcodes.Outgoing.NAME_REJECTED);
		super.writeStr(name);
		super.writeStr(message);
	}
}
