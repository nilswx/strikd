package strikd.communication.outgoing;

import strikd.communication.Opcodes;
import strikd.net.codec.OutgoingMessage;

public class NameChangedMessage extends OutgoingMessage
{
	public NameChangedMessage(String newName)
	{
		super(Opcodes.Outgoing.NAME_CHANGED);
		super.writeStr(newName);
	}
}
