package strikd.game.items;

import java.util.Date;

import org.apache.log4j.Logger;

public enum ItemType
{
	// Powerups
	FREEZE(ItemKind.POWERUP),
	JUNIOR_ASIAN(ItemKind.POWERUP),
	INTERMEDIATE_ASIAN(ItemKind.POWERUP),
	ARTISAN_ASIAN(ItemKind.POWERUP),
	
	// Avatar parts
	BDAYHAT(ItemKind.AVATAR_PART);
	
	
	public enum ItemKind
	{
		POWERUP,
		AVATAR_PART,
		TROPHY
	}
	
	public final ItemKind kind;
	
	ItemType(ItemKind kind)
	{
		this.kind = kind;
	}
	
	public ItemInstance newInstance()
	{
		ItemInstance item = new ItemInstance();
		item.type = this;
		item.timestamp = new Date();
		
		return item;
	}
	
	private static final Logger logger = Logger.getLogger(ItemType.class);
	
	public static void debugTypes()
	{
		for(ItemType type : ItemType.values())
		{
			logger.debug(String.format("registered %s (type=%s)", type, type.kind));
		}
	}
}
