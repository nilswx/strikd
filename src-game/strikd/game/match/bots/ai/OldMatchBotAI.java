package strikd.game.match.bots.ai;

import java.util.List;

import strikd.game.board.Board;
import strikd.game.board.Tile;
import strikd.game.match.Match;
import strikd.game.match.bots.MatchBotPlayer;

public abstract class OldMatchBotAI
{
	private final MatchBotPlayer bot;
	
	public OldMatchBotAI(MatchBotPlayer player)
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
