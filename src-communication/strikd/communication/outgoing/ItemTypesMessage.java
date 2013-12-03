package strikd.communication.outgoing;

import java.util.Collection;

import strikd.communication.Opcodes;
import strikd.game.items.AvatarPart;
import strikd.game.items.ItemType;
import strikd.game.items.PowerUp;
import strikd.game.items.Trophy;
import strikd.net.codec.OutgoingMessage;

public class ItemTypesMessage extends OutgoingMessage
{
	private static final byte TROPHY = 't';
	private static final byte POWER_UP = 'p';
	private static final byte AVATAR_PART = 'a';
	
	public ItemTypesMessage(Collection<ItemType> types)
	{
		super(Opcodes.Outgoing.ITEM_TYPES);
		super.writeInt(types.size());
		for(ItemType type : types)
		{
			super.writeInt(type.getId());
			super.writeStr(type.getCode());
			if(type instanceof Trophy)
			{
				super.writeByte(TROPHY);
			}
			else if(type instanceof PowerUp)
			{
				super.writeByte(POWER_UP);
			}
			else if(type instanceof AvatarPart)
			{
				super.writeByte(AVATAR_PART);
				super.writeByte((byte)((AvatarPart)type).getType().ordinal());
			}
		}
	}
}
