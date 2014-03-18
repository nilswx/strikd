package strikd.game.match;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import strikd.Server;
import strikd.game.match.bots.MatchBotFactory;
import strikd.game.match.queues.PlayerQueue;
import strikd.game.match.queues.TimedPlayerQueue;
import strikd.locale.LocaleBundle;
import strikd.locale.LocaleBundleManager;
import strikd.sessions.Session;
import strikd.util.NamedThreadFactory;

public class MatchManager extends Server.Referent
{
	private static final Logger logger = LoggerFactory.getLogger(MatchManager.class);
	
	private final AtomicLong matchCounter = new AtomicLong();
	private final Map<Long, Match> active = new ConcurrentHashMap<Long, Match>();
	private final Map<String, PlayerQueue> queues = new HashMap<String, PlayerQueue>(); 
	
	private final ScheduledExecutorService scheduler = 
			Executors.newScheduledThreadPool(1, new NamedThreadFactory("MatchManager Scheduler #%d"));
	
	public MatchManager(Server server)
	{
		super(server);
		for(LocaleBundle locale : this.getServer().getLocaleMgr().getBundles())
		{
			PlayerQueue queue = new TimedPlayerQueue(locale, this, 2, new MatchBotFactory());
			this.queues.put(locale.getLocale(), queue);
			
			logger.info("{}: created {}", locale, queue.getClass().getName());
		}
	}
	
	public void destroyMatch(long matchId)
	{
		Match match = this.active.remove(matchId);
		if(match != null)
		{
			// Record statistics etc
			this.writeMatchJournal(match);
			
			// Destroy this match
			match.destroy();
			logger.info("destroyed {}", match);
			
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
			return this.queues.get(session.getPlayer().getLocale());
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

	public Match newMatch(MatchPlayer playerOne, MatchPlayer playerTwo)
	{
		// Allow new matches?
		if(this.getServer().isShutdownMode())
		{
			// No, no, no...
			return null;
		}
		
		// Locale mismatch?
		String matchLocale = playerOne.getInfo().getLocale();
		if(!playerTwo.getInfo().getLocale().equals(matchLocale))
		{
			// Just what the fuck is going on with you, anyway
			return null;
		}
		
		// Resolve locale bundle
		LocaleBundleManager locMgr = super.getServer().getLocaleMgr();
		LocaleBundle locale = locMgr.getBundle(matchLocale);
		if(locale == null)
		{
			// BRO.
			return null;
		}
			
		// Initialize a new match with given details and a fresh match ID
		long matchId = this.matchCounter.incrementAndGet();
		Match match = new Match(matchId, locale, this, playerOne, playerTwo);
		
		// Register match
		logger.info("created {}", match);
		this.active.put(matchId, match);
		
		// Notify players of the new match
		match.announce();
		
		// Prepare the board
		match.prepareBoard();
		
		// Match ok!
		return match;
	}
	
	public synchronized void shutdownQueues(String message)
	{
		logger.debug("shutting down queues");
		
		for(PlayerQueue queue : this.queues.values())
		{
			queue.close();
		}
		this.queues.clear();
	}
	
	private void writeMatchJournal(Match match)
	{
		logger.debug("writing match journal for {}", match);
		
		// TODO: stats
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
	
	public ScheduledExecutorService getScheduler()
	{
		return this.scheduler;
	}
}
