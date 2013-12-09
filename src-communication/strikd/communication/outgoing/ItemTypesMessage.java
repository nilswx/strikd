package strikd.communication.outgoing;

import java.util.Collection;

import strikd.communication.Opcodes;
import strikd.game.items.AvatarPart;
import strikd.game.items.ItemType;
import strikd.net.codec.OutgoingMessage;

public class ItemTypesMessage extends OutgoingMessage
{
	public ItemTypesMessage(Collection<ItemType> types)
	{
		super(Opcodes.Outgoing.ITEM_TYPES);
		super.writeInt(types.size());
		for(ItemType type : types)
		{
			super.writeInt(type.getId());
			super.writeStr(type.getCode());
			super.writeByte(type.getTypeChar());
			if(type instanceof AvatarPart)
			{
				super.writeByte(((AvatarPart)type).getSlot().ordinal());
			}
		}
	}
}
