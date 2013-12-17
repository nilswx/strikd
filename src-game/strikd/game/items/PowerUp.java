package strikd.game.items;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import strikd.game.match.MatchPlayer;

/**
 * Activated during a match. Fire & forget: has a temporary effect and then disappears.
 */
public abstract class PowerUp extends ItemType
{
	private static final Logger logger = LoggerFactory.getLogger(PowerUp.class);
	
	public PowerUp(int id, String code)
	{
		super('p', id, code);
	}
	
	public final void activate(MatchPlayer player, String... args)
	{
		logger.debug("{} will activate {} with args={}", player, this.getCode(), args);
		
		try
		{
			this.onActivate(player, args);
		}
		catch(Exception e)
		{
			logger.error("error activating {}", this, e);
		}
	}
	
	protected abstract void onActivate(MatchPlayer player, String... args);
}
