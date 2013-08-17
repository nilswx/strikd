package strikd.game.match;

import strikd.communication.outgoing.AnnounceMatchMessage;
import strikd.communication.outgoing.BoardUpdateMessage;
import strikd.communication.outgoing.StartMatchMessage;
import strikd.game.board.Board;
import strikd.game.board.GappieBoard;
import strikd.net.codec.OutgoingMessage;

public class Match
{
	private final long matchId;
	private final MatchPlayer players[];
	private final MatchTimer timer;
	private final Board board;
	
	private final byte loadingTime;
	
	public Match(long matchId, MatchPlayer... players)
	{
		this.matchId = matchId;
		this.players = players;
		this.timer = new MatchTimer(2 * 60);
		this.board = new GappieBoard(20, 20);
		this.loadingTime = 5;
		
		// Assign unique actor IDs
		for(int actorId = 0; actorId < players.length; actorId++)
		{
			players[actorId].setMatch(actorId, this);
		}
	}
	
	public void destroy()
	{
		// TODO Auto-generated method stub		
	}
	
	public void announce()
	{
		this.players[0].send(new AnnounceMatchMessage(this, this.players[0], this.players[1]));
		this.players[1].send(new AnnounceMatchMessage(this, this.players[1], this.players[0]));
	}
	
	public void broadcast(OutgoingMessage msg)
	{
		for(MatchPlayer player : this.players)
		{
			player.send(msg);
		}
	}
	
	public void checkReady()
	{
		for(MatchPlayer player : this.players)
		{
			if(!player.isReady())
			{
				return;
			}
		}
		
		this.start();
	}
	
	public void start()
	{
		// Initial board!
		this.broadcast(new BoardUpdateMessage(1, this.board));
		
		// Start the timers at the clients etc, the game is ON!
		this.broadcast(new StartMatchMessage());
		
		// TODO: start serverside match loop for bot AI etc?
	}
	
	public boolean isEnded()
	{
		return this.timer.isDone();
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
	
	public byte getLoadingTime()
	{
		return this.loadingTime;
	}	
}
