package strikd.game.items;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum ItemType
{
	/**
	 * Freezes the opponent's screen with removable for x seconds.
	 */
	FREEZE(ItemKind.POWERUP),
	
	/**
	 * Fills the opponent's screen with pourable water for x seconds.
	 */
	WATER(ItemKind.POWERUP),
	
	/**
	 * Fills the opponent's screen with removable sand for x seconds.
	 */
	SAND(ItemKind.POWERUP),
	
	/**
	 * Auto-solver AI for x seconds.
	 */
	JUNIOR_ASIAN(ItemKind.POWERUP),
	
	/**
	 * Better and longer lasting auto-solver AI.
	 */
	INTERMEDIATE_ASIAN(ItemKind.POWERUP),
	
	/**
	 * Best auto-solver AI.
	 */
	ARTISAN_ASIAN(ItemKind.POWERUP),
	
	/**
	 * Also receives 50% of the opponent's score for x seconds.
	 */
	SNITCH(ItemKind.POWERUP),
	
	/**
	 * Select and destroy 3 arbitrary tiles.
	 */
	HAMMER_3(ItemKind.POWERUP),
	
	/**
	 * Select and destroy 5 arbitrary tiles.
	 */
	HAMMER_5(ItemKind.POWERUP),
	
	/**
	 * Received when the user logs in on his/her Facebook birthday.
	 */
	BDAYHAT(ItemKind.AVATAR_PART),
	
	/**
	 * Received after 5 matches are won.
	 */
	WINS_5(ItemKind.TROPHY),
	
	/**
	 * Received after 15 matches are won.
	 */
	WINS_10(ItemKind.TROPHY),
	
	/**
	 * Received after one friend is beaten. 
	 */
	FRIEND_BEAT(ItemKind.TROPHY),
	
	/**
	 * Received after liking Strik on Facebook. Thanks!
	 */
	LIKED(ItemKind.TROPHY);
	
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
	
	private static final Logger logger = LoggerFactory.getLogger(ItemType.class);
	
	public static void debugTypes()
	{
		for(ItemType type : ItemType.values())
		{
			logger.debug(String.format("registered %s (type=%s)", type, type.kind));
		}
	}
}
