package strikd.communication.outgoing;

import java.util.List;

import strikd.communication.Opcodes;
import strikd.game.items.Item;
import strikd.net.codec.OutgoingMessage;

public class ItemsMessage extends OutgoingMessage
{
	public ItemsMessage(List<Item> items)
	{
		super(Opcodes.Outgoing.ITEMS);
		super.writeInt(items.size());
		for(Item item : items)
		{
			super.writeInt(item.id);
			super.writeInt(item.typeId);
			super.writeLong(item.timestamp.getTime());
			super.writeStr(item.data);
		}
	}
}
