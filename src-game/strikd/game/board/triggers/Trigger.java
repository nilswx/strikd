package strikd.game.board.triggers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import strikd.game.board.Tile;
import strikd.game.match.MatchPlayer;

public abstract class Trigger
{
	private static final Logger logger = LoggerFactory.getLogger(Trigger.class);
	
	public abstract String getTypeName();

	public final void activate(MatchPlayer player, Tile tile)
	{
		try
		{
			this.onActivate(player, tile);
		}
		catch(Exception e)
		{
			logger.error("error firing {} on tile {}", this, tile, e);
		}
	}
	
	protected abstract void onActivate(MatchPlayer player, Tile tile);
}
