package strikd.game.match.queues;

import strikd.game.match.MatchManager;
import strikd.sessions.Session;

public abstract class PlayerQueue
{
	private final MatchManager matchMgr;
	
	protected PlayerQueue(MatchManager maker)
	{
		this.matchMgr = maker;
	}
	
	public MatchManager getMaker()
	{
		return this.matchMgr;
	}
	
	public abstract Entry enqueue(Session session);
	
	public abstract void dequeue(Entry entry);
	
	public static abstract class Entry
	{
		private final Session session;
		private final PlayerQueue queue;
		
		public Entry(Session session, PlayerQueue queue)
		{
			this.session = session;
			this.queue = queue;
		}
		
		public void exit()
		{
			this.queue.dequeue(this);
		}
		
		public Session getSession()
		{
			return this.session;
		}
	}
}