package strikd.communication.outgoing;

import strikd.communication.Opcodes;
import strikd.game.items.Item;
import strikd.net.codec.OutgoingMessage;

public class ItemAddedMessage extends OutgoingMessage
{
	public ItemAddedMessage(Item item)
	{
		super(Opcodes.Outgoing.ITEM_ADDED);
		super.writeInt(item.id);
		super.writeInt(item.typeId);
		super.writeLong(item.timestamp.getTime());
		super.writeStr(item.data);
	}
}
