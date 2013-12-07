package strikd.game.items.powerup;

import strikd.game.items.PowerUp;
import strikd.game.match.MatchPlayer;

/**
 * Yield 50% of the opponent's score for x seconds.
 */
public class SnitchPowerUp extends PowerUp
{
	public SnitchPowerUp(int id, String code)
	{
		super(id, code);
	}

	@Override
	protected void onActivate(MatchPlayer player, String... args)
	{
		// Limited time: yield 50% of opponents score additions
	}
}
