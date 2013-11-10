package strikd.game.match.bots;

import java.util.List;

import strikd.game.board.Board;
import strikd.game.board.Tile;
import strikd.game.match.Match;

public abstract class MatchBotAI
{
	private final MatchBotPlayer bot;
	
	public MatchBotAI(MatchBotPlayer player)
	{
		this.bot = player;
	}
	
	public abstract List<Tile> nextMove();
	
	public abstract int getNextMoveDelay();
	
	public MatchBotPlayer getPlayer()
	{
		return this.bot;
	}
	
	protected Match getMatch()
	{
		return this.bot.getMatch();
	}
	
	protected Board getBoard()
	{
		return this.getMatch().getBoard();
	}
}
