package strikd.game.items;

import strikd.sessions.Session;

/**
 * Can be equipped to the player's avatar. Remains in the inventory. Some parts have an effect while being worn.
 */
public abstract class AvatarPart extends ItemType
{
	private final PartSlot slot;
	
	public AvatarPart(int id, String code, PartSlot slot)
	{
		super('a', id, code);
		this.slot = slot;
	}
	
	public abstract void onEquip(Session session);
	
	public abstract void onRemove(Session session);
	
	public PartSlot getSlot()
	{
		return this.slot;
	}
	
	public enum PartSlot
	{
		HAT,
		HAIR,
		EYES,
		MOUTH,
		HEAD,
		BASE
	}
}
