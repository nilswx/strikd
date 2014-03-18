package strikd.game.match.queues;

import java.util.concurrent.TimeUnit;

import strikd.communication.outgoing.QueueEnteredMessage;
import strikd.communication.outgoing.QueueExitedMessage;
import strikd.game.match.Match;
import strikd.game.match.MatchManager;
import strikd.game.match.MatchPlayer;
import strikd.locale.LocaleBundle;
import strikd.sessions.Session;

public abstract class PlayerQueue implements Iterable<PlayerQueue.Entry>
{
	private final LocaleBundle locale;
	private final MatchManager matchMgr;
	
	protected PlayerQueue(LocaleBundle locale, MatchManager matchMgr)
	{
		this.locale = locale;
		this.matchMgr = matchMgr;
	}
	
	public abstract PlayerQueue.Entry enqueue(Session session);
	
	public abstract void dequeue(Entry entry);
	
	protected Match newMatch(MatchPlayer playerOne, MatchPlayer playerTwo)
	{
		return this.matchMgr.newMatch(playerOne, playerTwo);
	}
	
	public void close()
	{
		for(PlayerQueue.Entry player : this)
		{
			player.exit();
		}
	}
	
	public LocaleBundle getLocale()
	{
		return this.locale;
	}
	
	public MatchManager getMatchMgr()
	{
		return this.matchMgr;
	}
	
	public String getName()
	{
		return this.locale.getLocale();
	}
	
	public abstract int getAvgWaitingTime();
	
	public static class Entry
	{
		private final Session session;
		private final PlayerQueue queue;
		private final long timestamp;
		
		public Entry(Session session, PlayerQueue queue)
		{
			this.session = session;
			this.queue = queue;
			this.timestamp = System.currentTimeMillis();
			
			session.send(new QueueEnteredMessage(queue));
		}
		
		public void exit()
		{
			this.queue.dequeue(this);
			
			this.session.send(new QueueExitedMessage());
		}
		
		public long getTimestamp()
		{
			return this.timestamp;
		}
		
		public int getWaitingSeconds()
		{
			return (int) TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - this.timestamp);
		}
		
		public Session getSession()
		{
			return this.session;
		}
		
		public MatchPlayer toMatchPlayer()
		{
			return new MatchPlayer(this.session);
		}
	}
}