package strikd.communication.outgoing;

import java.util.List;

import strikd.communication.Opcodes;
import strikd.game.items.ItemInstance;
import strikd.net.codec.OutgoingMessage;

public class ItemsAddedMessage extends OutgoingMessage
{
	public ItemsAddedMessage(List<ItemInstance> items)
	{
		super(Opcodes.Outgoing.ITEMS_ADDED);
		super.writeInt(items.size());
		for(ItemInstance item : items)
		{
			ItemsMessage.serializeItem(item, this);
		}
	}
}
