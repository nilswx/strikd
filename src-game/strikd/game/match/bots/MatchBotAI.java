package strikd.game.match.bots;

import strikd.game.board.Board;
import strikd.game.match.Match;

public abstract class MatchBotAI
{
	private final MatchBotPlayer bot;
	
	public MatchBotAI(MatchBotPlayer player)
	{
		this.bot = player;
	}
	
	public abstract void nextMove();
	
	public abstract int nextMoveDelay();
	
	public final MatchBotPlayer getPlayer()
	{
		return this.bot;
	}
	
	protected final Match getMatch()
	{
		return this.bot.getMatch();
	}
	
	protected final Board getBoard()
	{
		return this.getMatch().getBoard();
	}
}
