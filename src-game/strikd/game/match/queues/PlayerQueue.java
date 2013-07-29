package strikd.game.match.queues;

import strikd.game.match.MatchMaker;
import strikd.sessions.Session;

public abstract class PlayerQueue
{
	private final MatchMaker maker;
	
	protected PlayerQueue(MatchMaker maker)
	{
		this.maker = maker;
	}
	
	public MatchMaker getMaker()
	{
		return this.maker;
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