package strikd.game.match;

import strikd.game.board.AbstractBoard;
import strikd.game.board.GappieBoard;
import strikd.net.codec.StrikMessage;

public class Match
{
	private final long matchId;
	private final MatchPlayer players[];
	private final MatchTimer timer;
	private final AbstractBoard board;
	
	public Match(long matchId, MatchPlayer playerOne, MatchPlayer playerTwo)
	{
		this.matchId = matchId;
		this.players = new MatchPlayer[] { playerOne, playerTwo };
		this.timer = new MatchTimer(2 * 60);
		this.board = new GappieBoard(20, 20);
	}
	
	public void destroy()
	{
		// TODO Auto-generated method stub		
	}
	
	public void broadcast(StrikMessage msg)
	{
		for(MatchPlayer player : this.players)
		{
			player.getSession().send(msg);
		}
	}

	private boolean isExtraTimeActive()
	{
		// Test for players that have their extra timer running
		for(MatchPlayer player : this.players)
		{
			if(!player.getExtraTimer().isDone())
			{
				return true;
			}
		}
		
		return false;
	}
	
	public boolean isEnded()
	{
		return this.timer.isDone() && !this.isExtraTimeActive();
	}
	
	public long getMatchId()
	{
		return this.matchId;
	}
	
	public MatchPlayer getPlayerOne()
	{
		return this.players[0];
	}
	
	public MatchPlayer getPlayerTwo()
	{
		return this.players[0];
	}
	
	public MatchTimer getTimer()
	{
		return this.timer;
	}
	
	public AbstractBoard getBoard()
	{
		return this.board;
	}
}
