package strikd.game.match.queues;

import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.google.common.collect.Queues;

import strikd.game.match.MatchManager;
import strikd.game.match.MatchPlayer;
import strikd.game.match.bots.MatchBotFactory;
import strikd.locale.LocaleBundle;
import strikd.sessions.Session;

public class TimedPlayerQueue extends PlayerQueue
{
	private static final long MATCHMAKING_INTERVAL = 500;
	
	private final Queue<TimedPlayerQueue.Entry> players = Queues.newConcurrentLinkedQueue();
	private final ScheduledFuture<?> schedulerTask;
	
	private final int maxWaitingSeconds;
	private final MatchBotFactory botFactory;
	
	public TimedPlayerQueue(LocaleBundle locale, MatchManager matchMgr, int maxWaitingSeconds, MatchBotFactory botFactory)
	{
		super(locale, matchMgr);
		this.schedulerTask = matchMgr.getScheduler().scheduleWithFixedDelay(new Runnable()
		{
			@Override
			public void run()
			{
				TimedPlayerQueue.this.makeMatches();
			}
		}, 0, MATCHMAKING_INTERVAL, TimeUnit.MILLISECONDS);
		
		this.maxWaitingSeconds = maxWaitingSeconds;
		this.botFactory = botFactory;
	}
	
	@Override
	public void close()
	{
		this.schedulerTask.cancel(false);
		super.close();
	}
	
	@Override
	public Entry enqueue(Session session)
	{
		Entry entry = new Entry(session, this);
		this.players.add(entry);
		
		return entry;
	}

	@Override
	public void dequeue(PlayerQueue.Entry entry)
	{
		this.players.remove(entry);
	}
	
	@Override
	public Iterator<PlayerQueue.Entry> iterator()
	{
		return null;//((Queue<PlayerQueue.Entry>)this.players).iterator();
	}
	
	private void makeMatches()
	{
		// Connect waiting players (simple FIFO fashion)
		Entry waiting = null;
		for(Entry entry : this.players)
		{
			if(waiting != null)
			{
				super.newMatch(waiting.toMatchPlayer(), entry.toMatchPlayer());
				waiting = null;
			}
			else
			{
				waiting = entry;
			}
		}
		
		// Max waiting time reached? How about a bot opponent!
		for(Entry entry : this.players)
		{
			if(entry.getWaitingSeconds() >= this.maxWaitingSeconds)
			{
				MatchPlayer player = entry.toMatchPlayer();
				super.newMatch(player, this.botFactory.newBotForOpponent(player));
			}
		}
	}
	
	@Override
	public int getAvgWaitingTime()
	{
		return 0;
	}
}
