package strikd.communication.outgoing;

import java.util.List;

import strikd.communication.Opcodes;
import strikd.game.items.ItemInstance;
import strikd.net.codec.OutgoingMessage;

public class ItemsMessage extends OutgoingMessage
{
	public ItemsMessage(List<ItemInstance> items)
	{
		super(Opcodes.Outgoing.ITEMS);
		super.writeInt(items.size());
		for(ItemInstance item : items)
		{
			serializeItem(item, this);
		}
	}
	
	public static void serializeItem(ItemInstance item, OutgoingMessage msg)
	{
		msg.writeInt(item.id);
		msg.writeInt(item.typeId);
		msg.writeLong(item.timestamp.getTime());
	}
}
