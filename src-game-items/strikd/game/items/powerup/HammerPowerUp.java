package strikd.game.items.powerup;

import strikd.game.items.PowerUp;
import strikd.game.match.MatchPlayer;

/**
 * Select and destroy 1 arbitrary tile.
 */
public class HammerPowerUp extends PowerUp
{
	public HammerPowerUp(int id, String code)
	{
		super(id, code);
	}

	@Override
	public void onActivate(MatchPlayer player, String... args)
	{
		// Destroy the specified tile
	}
}
