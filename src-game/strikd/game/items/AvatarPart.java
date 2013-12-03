package strikd.game.items;

import strikd.sessions.Session;

/**
 * Can be equipped to the player's avatar. Remains in the inventory. Some parts have an effect while being worn.
 */
public abstract class AvatarPart extends ItemType
{
	public AvatarPart(int id, String code)
	{
		super(id, code);
	}
	
	public abstract void onEquip(Session session);
	
	public abstract void onRemove(Session session);
}
