package strikd.game.items;

/**
 * The stateless definition of an item in the game. Every item has a unique ID and code and its behavior is usually described by an {@link ItemKind}.
 * 
 * @see ItemTypeRegistry
 * @see ItemInventory
 */
public abstract class ItemType
{
	private final int id;
	private final String code;
	
	public ItemType(int id, String code)
	{
		this.id = id;
		this.code = code;
	}
	
	public int getId()
	{
		return this.id;
	}
	
	public String getCode()
	{
		return this.code;
	}
}