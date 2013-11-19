package strikd.game.board.triggers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import strikd.game.match.MatchPlayer;

public class BombTrigger extends Trigger
{
	private static final Logger logger = LoggerFactory.getLogger(BombTrigger.class);
	
	@Override
	public String getTypeName()
	{
		return "bomb";
	}

	@Override
	public void execute(MatchPlayer player)
	{
		logger.debug(String.format("Player %s says BOOOOOM!", player));
	}
}
