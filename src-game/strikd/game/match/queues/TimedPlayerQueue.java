package strikd.game.match.queues;

import strikd.game.match.MatchMaker;
import strikd.sessions.Session;

public class TimedPlayerQueue extends PlayerQueue
{
	public TimedPlayerQueue(MatchMaker maker)
	{
		super(maker);
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