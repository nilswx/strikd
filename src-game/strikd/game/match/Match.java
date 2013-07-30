package strikd.game.match;

import strikd.game.board.Board;
import strikd.game.board.GappieBoard;
import strikd.net.codec.OutgoingMessage;

public class Match
{
	private final long matchId;
	private final MatchPlayer players[];
	private final MatchTimer timer;
	private final Board board;
	
	private final byte musicId;
	private final byte loadingTime;
	
	public Match(long matchId, MatchPlayer... players)
	{
		this.matchId = matchId;
		this.players = players;
		this.timer = new MatchTimer(2 * 60);
		this.board = new GappieBoard(20, 20);
		
		this.musicId = 1;
		this.loadingTime = 5;
	}
	
	public void destroy()
	{
		// TODO Auto-generated method stub		
	}
	
	public void broadcast(OutgoingMessage msg)
	{
		for(MatchPlayer player : this.players)
		{
			if(!(player instanceof MatchBotPlayer))
			{
				player.getSession().send(msg);
			}
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
	
	public MatchPlayer[] getPlayers()
	{
		return this.players;
	}
	
	public MatchTimer getTimer()
	{
		return this.timer;
	}
	
	public Board getBoard()
	{
		return this.board;
	}
	
	public byte getMusicId()
	{
		return this.musicId;
	}
	
	public byte getLoadingTime()
	{
		return this.loadingTime;
	}
}
