package strikd.game.items.powerup;

import strikd.game.items.PowerUp;
import strikd.game.match.MatchPlayer;

/**
 * Freezes the opponent's screen with removable ice for x seconds.
 */
public class FreezePowerUp extends PowerUp
{
	public FreezePowerUp(int id, String code)
	{
		super(id, code);
	}

	@Override
	public void onActivate(MatchPlayer player, String... args)
	{
		// Freeze opponents screen blabla
	}
}
