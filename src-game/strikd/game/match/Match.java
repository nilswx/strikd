package strikd.game.match;

import java.util.concurrent.TimeUnit;

import strikd.Server;
import strikd.communication.outgoing.AnnounceMatchMessage;
import strikd.communication.outgoing.MatchStartedMessage;
import strikd.game.board.Board;
import strikd.game.board.impl.BruteBoard;
import strikd.locale.LocaleBundle;
import strikd.locale.LocaleBundle.DictionaryType;
import strikd.net.codec.OutgoingMessage;

public class Match
{
	private final long matchId;
	private final LocaleBundle locale;
	
	private final MatchPlayer playerOne;
	private final MatchPlayer playerTwo;
	
	private final MatchTimer timer;
	private final Board board;
	
	private final byte loadingTime;
	private long startTime;
	
	public Match(long matchId, LocaleBundle locale, MatchPlayer playerOne, MatchPlayer playerTwo)
	{
		// Setup static match data
		this.matchId = matchId;
		this.locale = locale;
		this.loadingTime = 0;
		
		// Setup timer
		this.timer = new MatchTimer((int)TimeUnit.MINUTES.toSeconds(2));
		
		// Install the board implementation
		this.board = new BruteBoard(5, 6, locale.getDictionary(DictionaryType.GENERATOR));
		
		// Link the players to this match with a personal ID
		this.playerOne = playerOne.setMatch(this, 1);
		this.playerTwo = playerTwo.setMatch(this, 2);
		
		// Generate board
		this.board.regenerate();
		System.out.println(this.board.toMatrixString());
		
		// Record start time
		this.startTime = System.currentTimeMillis();
	}
	
	public void destroy()
	{
		
	}
	
	public void announce()
	{
		// Send personal ANNOUNCE messages to both players
		this.playerOne.send(new AnnounceMatchMessage(this, this.playerOne, this.playerTwo));
		this.playerTwo.send(new AnnounceMatchMessage(this, this.playerTwo, this.playerOne));
	}
	
	public void broadcast(OutgoingMessage msg)
	{
		// Send to both players
		this.playerOne.send(msg);
		this.playerTwo.send(msg);
	}
	
	public void checkReady()
	{
		// Both players ready?
		if(this.playerOne.isReady() && this.playerTwo.isReady())
		{
			this.start();
		}
	}
	
	private void start()
	{
		// Not already started?
		if(this.startTime == 0)
		{
			// Initial board!
			this.broadcast(this.board.getUpdateGenerator().generateUpdates());
			
			// Start the timers at the clients etc, the game is ON!
			this.broadcast(new MatchStartedMessage());
			
			// Record start time
			this.startTime = System.currentTimeMillis();
		}
	}

	public void removePlayer(MatchPlayer player)
	{
		// The given player loses...
		// TODO: lose
		
		// ... and the match is destroyed!
		Server server = player.getSession().getServer();
		server.getMatchMgr().destroyMatch(this.matchId);
	}
	
	public boolean isEnded()
	{
		return this.timer.isDone();
	}
	
	public long getMatchId()
	{
		return this.matchId;
	}
	
	public LocaleBundle getLocale()
	{
		return this.locale;
	}
	
	public MatchPlayer getOpponent(MatchPlayer player)
	{
		return (player == this.playerOne) ? this.playerTwo : this.playerOne;
	}
	
	public MatchPlayer[] getPlayers()
	{
		return new MatchPlayer[] { this.playerOne, this.playerTwo };
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
	
	public long getStartTime()
	{
		return this.startTime;
	}
	
	@Override
	public String toString()
	{
		return String.format("match #%d (%s)", this.matchId, this.locale);
	}
}
