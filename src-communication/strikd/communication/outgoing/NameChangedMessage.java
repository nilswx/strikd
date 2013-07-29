package strikd.communication.outgoing;

import strikd.communication.Opcodes;
import strikd.net.codec.OutgoingMessage;

public class NameChangedMessage extends OutgoingMessage
{
	public NameChangedMessage(boolean success, String message, String newName)
	{
		super(Opcodes.Outgoing.NAME_CHANGED);
		super.writeBool(success);
		super.writeStr(message);
		super.writeStr(newName);
	}
}
