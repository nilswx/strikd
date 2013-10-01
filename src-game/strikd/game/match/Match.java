package strikd.game.match;

import java.util.concurrent.TimeUnit;

import strikd.Server;
import strikd.communication.outgoing.AnnounceMatchMessage;
import strikd.communication.outgoing.StartMatchMessage;
import strikd.game.board.Board;
import strikd.game.board.BruteBoard;
import strikd.locale.LocaleBundle;
import strikd.locale.LocaleBundle.DictionaryType;
import strikd.net.codec.OutgoingMessage;

public class Match
{
	private final long matchId;
	private final LocaleBundle locale;
	private final MatchPlayer players[];
	private final MatchTimer timer;
	private final Board board;
	
	private final byte loadingTime;
	private final long startTime;
	
	public Match(long matchId, LocaleBundle locale, MatchPlayer... players)
	{
		this.matchId = matchId;
		this.locale = locale;
		this.players = players;
		
		// The round length for this match
		this.timer = new MatchTimer((int)TimeUnit.MINUTES.toSeconds(2));
		
		// Install the board generation algorithm for this match
		this.board = new BruteBoard(5, 6, locale.getDictionary(DictionaryType.GENERATOR));
		
		// The loading/advertisement time for this match (0 for development)
		this.loadingTime = 0;
		
		// Assign unique IDs to all players
		for(int playerId = 0; playerId < players.length; playerId++)
		{
			players[playerId].setMatch(this, playerId);
		}
		
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
		this.broadcast(this.board.getUpdateGenerator().generateUpdates());
		
		// Start the timers at the clients etc, the game is ON!
		this.broadcast(new StartMatchMessage());
		
		// TODO: start serverside match loop for bot AI etc?
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
