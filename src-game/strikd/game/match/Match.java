package strikd.game.match;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import strikd.communication.outgoing.AnnounceMatchMessage;
import strikd.communication.outgoing.MatchEndedMessage;
import strikd.communication.outgoing.MatchStartedMessage;
import strikd.game.board.Board;
import strikd.game.board.impl.BruteBoard;
import strikd.locale.LocaleBundle;
import strikd.locale.LocaleBundle.DictionaryType;
import strikd.net.codec.OutgoingMessage;

public class Match
{
	private static final Logger logger = Logger.getLogger(Match.class);
	
	private final long matchId;
	private final LocaleBundle locale;
	private final MatchManager matchMgr;
	
	private final MatchPlayer playerOne;
	private final MatchPlayer playerTwo;
	
	private final MatchTimer timer;
	private final Board board;
	
	private final byte loadingTime;
	private long startTime;
	
	public Match(long matchId, LocaleBundle locale, MatchManager matchMgr, MatchPlayer playerOne, MatchPlayer playerTwo)
	{
		// Setup static match data
		this.matchId = matchId;
		this.locale = locale;
		this.matchMgr = matchMgr;
		
		// Setup timer
		this.loadingTime = 0;
		this.timer = new MatchTimer((int)TimeUnit.MINUTES.toSeconds(2));
		
		// Install the board implementation
		this.board = new BruteBoard(5, 6, locale.getDictionary(DictionaryType.GENERATOR));
		
		// Link the players to this match with a personal ID
		this.playerOne = playerOne.setMatch(this, 1);
		this.playerTwo = playerTwo.setMatch(this, 2);
		
		// Generate board
		this.board.regenerate();
		System.out.println(this.board.toMatrixString());
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
		if(!this.isStarted())
		{
			// Initial board!
			this.broadcast(this.board.getUpdateGenerator().generateUpdates());
			
			// Start the timers at the clients etc, the game is ON!
			this.broadcast(new MatchStartedMessage());
			
			// Record start time
			this.startTime = System.currentTimeMillis();
		}
	}


	public void end(MatchPlayer winner)
	{
		// Can be ended?
		if(this.isStarted())
		{
			// Draw?
			if(winner == null)
			{
				logger.debug(String.format("%s: draw...", this));
			}
			else
			{
				// Determine loser
				MatchPlayer loser = this.getOpponent(winner);
				
				// Update stats
				winner.getInfo().wins++;
				loser.getInfo().losses++;
				logger.debug(String.format("%s: %s wins, %s loses!", this, winner, loser));
				
				// Broadcast event
				this.broadcast(new MatchEndedMessage());
			}
			
			// Request match to be destroyed
			this.matchMgr.destroyMatch(this.matchId);
		}
	}
	
	public void removePlayer(MatchPlayer player)
	{
		this.end(this.getOpponent(player));
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
	
	public boolean isStarted()
	{
		return (this.startTime > 0);
	}
	
	@Override
	public String toString()
	{
		return String.format("match #%d (%s)", this.matchId, this.locale);
	}
}
