package strikd.game.items;

import strikd.sessions.Session;

/**
 * Can be equipped to the player's avatar. Remains in the inventory. Some parts have an effect while being worn.
 */
public abstract class AvatarPart extends ItemType
{
	private final PartType type;
	
	public AvatarPart(int id, String code, PartType type)
	{
		super(id, code);
		this.type = type;
	}
	
	public abstract void onEquip(Session session);
	
	public abstract void onRemove(Session session);
	
	public PartType getType()
	{
		return this.type;
	}
	
	public enum PartType
	{
		HAT("ht"),
		HAIR("hr"),
		EYES("ey"),
		MOUTH("mo"),
		HEAD("hd"),
		BASE("bd");
		
		final String code;
		
		private PartType(String code)
		{
			this.code = code;
		}
		
		public String code()
		{
			return this.code;
		}
	}
}
