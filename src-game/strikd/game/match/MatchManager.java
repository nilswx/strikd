package strikd.game.match;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.log4j.Logger;

public class MatchManager
{
	private static final Logger logger = Logger.getLogger(MatchManager.class);
	
	private final AtomicLong matchCounter = new AtomicLong();
	private final Map<Long, Match> active = new ConcurrentHashMap<Long, Match>(16, 0.75f, 8);
	private final MatchMaker matchMaker = new MatchMaker();
	
	public Match createMatch(MatchPlayer p1, MatchPlayer p2)
	{
		// Create match with next ID
		long matchId = this.matchCounter.incrementAndGet();
		Match match = new Match(matchId, p1, p2);
		
		// Map match to collection
		logger.info(String.format("created match #%d", matchId));
		this.active.put(matchId, match);
		
		return match;
	}
	
	public void destroyMatch(long matchId)
	{
		Match match = this.active.remove(matchId);
		if(match != null)
		{
			match.destroy();
			logger.info(String.format("destroyed match #%d", matchId));
		}
	}
	
	public Match getMatch(int matchId)
	{
		return this.active.get(matchId);
	}

	public long active()
	{
		return this.active.size();
	}
}
