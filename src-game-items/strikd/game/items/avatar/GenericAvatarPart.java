package strikd.game.items.avatar;

import strikd.game.items.AvatarPart;
import strikd.sessions.Session;

public class GenericAvatarPart extends AvatarPart
{
	public GenericAvatarPart(int id, String code)
	{
		super(id, code);
	}

	@Override
	public void onEquip(Session session)
	{
		// Nothing special
	}

	@Override
	public void onRemove(Session session)
	{
		// Nothing special
	}
}
