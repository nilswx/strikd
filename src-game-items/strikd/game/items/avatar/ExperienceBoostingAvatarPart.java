package strikd.game.items.avatar;

import strikd.game.items.AvatarPart;
import strikd.sessions.Session;

public class ExperienceBoostingAvatarPart extends AvatarPart
{
	public ExperienceBoostingAvatarPart(int id, String code, PartType type)
	{
		super(id, code, type);
	}

	@Override
	public void onEquip(Session session)
	{
		// Set XP boost float in session (string-based attachment system?)
	}

	@Override
	public void onRemove(Session session)
	{
		// Reset XP boost
	}
}
