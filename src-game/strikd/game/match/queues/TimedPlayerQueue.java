package strikd.game.match.queues;

import java.util.Iterator;

import strikd.game.match.MatchBotFactory;
import strikd.game.match.MatchManager;
import strikd.locale.LocaleBundle;
import strikd.sessions.Session;

public class TimedPlayerQueue extends PlayerQueue
{
	private final MatchBotFactory botFactory = new MatchBotFactory();
	
	public TimedPlayerQueue(LocaleBundle locale, MatchManager matchMgr)
	{
		super(locale, matchMgr);
	}

	@Override
	public PlayerQueue.Entry enqueue(Session session)
	{
		return new TimedPlayerQueue.Entry(session, this);
	}

	@Override
	public void dequeue(PlayerQueue.Entry entry)
	{
		
	}
	
	public int getAvgWaitingTime()
	{
		return 0;
	}
	
	@Override
	public Iterator<PlayerQueue.Entry> iterator()
	{
		return null;
	}
	
	private static class Entry extends PlayerQueue.Entry
	{
		private final long entryTime = System.currentTimeMillis();
		
		public Entry(Session session, PlayerQueue queue)
		{
			super(session, queue);
		}
		
		public long getWaitingTime()
		{
			return ((System.currentTimeMillis() - this.entryTime) / 1000);
		}
	}
}