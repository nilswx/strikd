package strikd.game.match.bots.ai;

import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;

import strikd.game.board.Tile;
import strikd.game.match.bots.MatchBotAI;
import strikd.game.match.bots.MatchBotPlayer;

public class SimpleMatchBotAI extends MatchBotAI
{
	private static final Logger logger = Logger.getLogger(SimpleMatchBotAI.class);
	
	public SimpleMatchBotAI(MatchBotPlayer player)
	{
		super(player);
		logger.toString();
	}

	@Override
	public int getNextMoveDelay()
	{
		int delay = new Random().nextInt(10*1000);
		//logger.debug(String.format("%s: I'M BREAKING A SWEAT (%d ms)", this.getPlayer(), delay));
		
		return delay;
	}
	
	@Override
	public List<Tile> nextMove()
	{
		//logger.debug(String.format("%s: IT'S ALRIGHT", this.getPlayer()));
		return null;
	}
}
