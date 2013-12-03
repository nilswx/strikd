package strikd.game.items.powerup;

import strikd.game.items.PowerUp;
import strikd.game.match.MatchPlayer;

/**
 * Fills the opponent's screen with pourable water for x seconds.
 */
public class WaterPowerUp extends PowerUp
{
	public WaterPowerUp(int id, String code)
	{
		super(id, code);
	}

	@Override
	public void onActivate(MatchPlayer player, String... args)
	{
		// Soak opponents screen blabla
	}
}
