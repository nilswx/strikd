package strikd.game.items.powerup;

import strikd.game.items.PowerUp;
import strikd.game.match.MatchPlayer;

/**
 * Temporarily overlays all tiles on the board with color hints for valid letter combinations.
 * Only visible to the user.
 */
public class ColorVisionPowerUp extends PowerUp
{
	public ColorVisionPowerUp(int id, String code)
	{
		super(id, code);
	}

	@Override
	protected void onActivate(MatchPlayer player, String... args)
	{
		// Send color accents
	}
}
