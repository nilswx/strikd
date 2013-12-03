package strikd.game.items.powerup;

import strikd.game.items.PowerUp;
import strikd.game.match.MatchPlayer;

/**
 * Allows the player to swap two tiles.
 */
public class SwapPowerUp extends PowerUp
{
	public SwapPowerUp(int id, String code)
	{
		super(id, code);
	}

	@Override
	public void onActivate(MatchPlayer player, String... args)
	{
		// Swap the specified tiles
	}
}
