package strikd.game.items;

import java.util.Date;

public class ItemType
{
	public int typeId;
	public ItemKind kind;
	
	public enum ItemKind
	{
		POWERUP,
		AVATAR_PART,
		TROPHY
	}
	
	public ItemInstance newInstance()
	{
		ItemInstance instance = new ItemInstance();
		instance.typeId = this.typeId;
		instance.timestamp = new Date();
		
		return instance;
	}
}
