package strikd.game.items;

import strikd.game.match.MatchPlayer;

/**
 * Activated during a match. Fire & forget: has a temporary effect and then disappears.
 */
public abstract class PowerUp extends ItemType
{
	public PowerUp(int id, String code)
	{
		super(id, code);
	}
	
	public abstract void onActivate(MatchPlayer player, String... args);
}
