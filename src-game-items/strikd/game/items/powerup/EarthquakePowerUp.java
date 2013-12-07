package strikd.game.items.powerup;

import strikd.game.items.PowerUp;
import strikd.game.match.MatchPlayer;

/**
 * Makes the opponents screen shake for x seconds, hindering selection!
 */
public class EarthquakePowerUp extends PowerUp
{
	public EarthquakePowerUp(int id, String code)
	{
		super(id, code);
	}

	@Override
	protected void onActivate(MatchPlayer player, String... args)
	{
		// Shake opponents screen blabla
	}
}
