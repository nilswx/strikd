package strikd.game.match.queues;

import strikd.game.match.MatchMaker;
import strikd.game.match.MatchPlayer;
import strikd.sessions.Session;

public class SimplePlayerQueue extends PlayerQueue
{
	private SimplePlayerQueue.Entry waiting;
	
	public SimplePlayerQueue(MatchMaker maker)
	{
		super(maker);
	}

	@Override
	public PlayerQueue.Entry enqueue(Session session)
	{
		Session opponent = null;
		synchronized(this.waiting)
		{
			if(this.waiting == null)
			{
				SimplePlayerQueue.Entry entry = new SimplePlayerQueue.Entry(session, this);
				this.waiting = entry;
				
				return entry;
			}
			else
			{
				opponent = this.waiting.getSession();
				this.waiting = null;
			}
		}
		
		// Was there an opponent waiting
		MatchPlayer p1 = new MatchPlayer(opponent);
		MatchPlayer p2 = new MatchPlayer(session);	
		this.getMaker().newMatch(p1, p2);
		
		// Not waiting
		return null;
	}

	@Override
	public void dequeue(PlayerQueue.Entry entry)
	{
		synchronized(this.waiting)
		{
			if(entry == this.waiting)
			{
				this.waiting = null;
			}
		}
	}
	
	private static class Entry extends PlayerQueue.Entry
	{
		public Entry(Session session, PlayerQueue queue)
		{
			super(session, queue);
		}
	}
}
