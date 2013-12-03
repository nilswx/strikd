package strikd.communication.outgoing;

import strikd.communication.Opcodes;
import strikd.net.codec.OutgoingMessage;

public class ItemsAddedMessage extends OutgoingMessage
{
	public ItemsAddedMessage()
	{
		super(Opcodes.Outgoing.ITEMS_ADDED);
	}
}
