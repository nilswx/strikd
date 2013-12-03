package strikd.game.items.powerup;

import strikd.game.items.PowerUp;
import strikd.game.match.MatchPlayer;

/**
 * Fills the opponent's screen with removable sand for x seconds.
 */
public class SandPowerUp extends PowerUp
{
	public SandPowerUp(int id, String code)
	{
		super(id, code);
	}

	@Override
	public void onActivate(MatchPlayer player, String... args)
	{
		// Drop sand on opponents screen blabla
	}
}
