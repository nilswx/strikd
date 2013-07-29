package strikd.communication.outgoing;

import strikd.communication.Opcodes;
import strikd.net.codec.OutgoingMessage;

public class NameChangedMessage extends OutgoingMessage
{
	public NameChangedMessage(boolean success, String newName, String message)
	{
		super(Opcodes.Outgoing.NAME_CHANGED);
		super.writeBool(success);
		super.writeStr(newName);
		super.writeStr(message);
	}
}
