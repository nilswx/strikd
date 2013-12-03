package strikd.communication.outgoing;

import strikd.communication.Opcodes;
import strikd.net.codec.OutgoingMessage;

public class ItemsMessage extends OutgoingMessage
{
	public ItemsMessage()
	{
		super(Opcodes.Outgoing.ITEMS);
	}
}
