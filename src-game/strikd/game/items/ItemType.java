package strikd.game.items;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static strikd.game.items.ItemType.ItemKind.*;

/**
 * An item in the game. Every item has a unique ID and an {@link ItemKind}.
 * 
 * 
 * @see ItemInventory
 */
public enum ItemType
{	
	/**
	 * Freezes the opponent's screen with removable for x seconds.
	 */
	FREEZE(1, POWERUP),
	
	/**
	 * Fills the opponent's screen with pourable water for x seconds.
	 */
	WATER(2, POWERUP),
	
	/**
	 * Fills the opponent's screen with removable sand for x seconds.
	 */
	SAND(3, POWERUP),
	
	/**
	 * Makes the opponents screen shake for x seconds, hindering selection!
	 */
	EARTHQUAKE(4, POWERUP),
	
	/**
	 * Allows the player to swap two tiles.
	 */
	SWAP(5, POWERUP),
	
	/**
	 * Auto-solver AI for x seconds.
	 */
	JUNIOR_ASIAN(6, POWERUP),
	
	/**
	 * Better and longer lasting auto-solver AI.
	 */
	INTERMEDIATE_ASIAN(7, POWERUP),
	
	/**
	 * Best auto-solver AI.
	 */
	ARTISAN_ASIAN(8, POWERUP),
	
	/**
	 * Also receives 50% of the opponent's score for x seconds.
	 */
	SNITCH(9, POWERUP),
	
	/**
	 * Select and destroy 1 arbitrary tile.
	 */
	HAMMER(10, POWERUP),
	
	
	
	
	
	
	/**
	 * Received when the user logs in on his/her Facebook birthday.
	 */
	HT_BDAY(200, AVATAR_PART),
	HT_MOUSE_EARS(201, AVATAR_PART),
	MO_BIG_MOUSTACHE(202, AVATAR_PART),
	EY_EYEPATCH(203, AVATAR_PART),
	
	
	
	
	/**
	 * Received after 5 matches are won.
	 */
	WINS_5(400, TROPHY),
	
	/**
	 * Received after 15 matches are won.
	 */
	WINS_10(401, TROPHY),
	
	/**
	 * Received after one friend is beaten. 
	 */
	FRIEND_BEAT(402, TROPHY),
	
	/**
	 * Received after liking Strik on Facebook. Thanks!
	 */
	LIKED(403, TROPHY);
	
	
	
	
	
	/**
	 * The kind of an {@link ItemType}. {@link ItemKind#POWERUP} items cannot be stacked.
	 */
	public enum ItemKind
	{
		/**
		 * Activated during a match. Fire & forget: has a temporary effect and then disappears.
		 */
		POWERUP,
		/**
		 * Can be equipped to the player's avatar. Remains in the inventory. Some parts have an effect while being worn.
		 */
		AVATAR_PART,
		/**
		 * Collectables that serve as achievements. Have no other use than just bragging rights.
		 */
		TROPHY
	}
	
	
	
	
	private final int id;
	private final ItemKind kind;
	
	ItemType(int id, ItemKind kind)
	{
		this.id = id;
		this.kind = kind;
	}
	
	public int id()
	{
		return this.id;
	}
	
	public ItemKind kind()
	{
		return this.kind;
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
			logger.debug("registered #{} {} (type={})", type.id(), type, type.kind());
		}
	}

	private static final ItemType[] items = new ItemType[1024];
	
	static
	{
		for(ItemType item : values())
		{
			items[item.id()] = item;
		}
	}
	
	public static ItemType byId(int typeId)
	{
		if(typeId >= 0 && typeId < items.length)
		{
			return items[typeId];
		}
		else
		{
			return null;
		}
	}
}
