package strikd.game.match;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.log4j.Logger;

import strikd.Server;
import strikd.game.match.queues.PlayerQueue;
import strikd.game.match.queues.SimplePlayerQueue;
import strikd.locale.LocaleBundle;
import strikd.sessions.Session;

public class MatchManager extends Server.Referent
{
	private static final Logger logger = Logger.getLogger(MatchManager.class);
	
	private final AtomicLong matchCounter = new AtomicLong();
	private final Map<Long, Match> active = new ConcurrentHashMap<Long, Match>(16, 0.75f, 8);
	private final Map<String, PlayerQueue> queues = new HashMap<String, PlayerQueue>(); 
	
	public MatchManager(Server server)
	{
		super(server);
		for(LocaleBundle locale : this.getServer().getLocaleMgr().getBundles())
		{
			PlayerQueue queue = new SimplePlayerQueue(this);
			this.queues.put(locale.getLocale(), queue);
			
			logger.debug(String.format("created %s for %s", queue.getClass().getSimpleName(), locale.getLocale()));
		}
	}
	
	public Match createMatch(MatchPlayer p1, MatchPlayer p2)
	{
		// Allow new matches?
		if(this.getServer().isShutdownMode())
		{
			return null;
		}
		
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
			// Destroy this match
			match.destroy();
			logger.info(String.format("destroyed match #%d", matchId));
			
			// Is this server in shutdown mode?
			if(this.getServer().isShutdownMode() && this.active() == 0)
			{
				this.getServer().destroy();
			}
		}
	}

	private PlayerQueue getQueue(Session session)
	{
		if(session.isLoggedIn())
		{
			return this.queues.get(session.getUser().language);
		}
		else
		{
			return null;
		}
	}
	
	public PlayerQueue.Entry requestMatch(Session session)
	{
		PlayerQueue queue = this.getQueue(session);
		if(queue != null)
		{
			PlayerQueue.Entry entry = queue.enqueue(session);
			if(entry != null)
			{
				return entry;
			}
		}
		
		return null;
	}

	public void newMatch(MatchPlayer... players)
	{
		
	}
	
	public Match getMatch(int matchId)
	{
		return this.active.get(matchId);
	}

	public int active()
	{
		return this.active.size();
	}

	public long matchCounter()
	{
		return this.matchCounter.longValue();
	}
}
