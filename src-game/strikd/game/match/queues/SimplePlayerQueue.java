package strikd.game.match.queues;

import java.util.Iterator;

import strikd.game.match.MatchManager;
import strikd.game.match.MatchPlayer;
import strikd.sessions.Session;

public class SimplePlayerQueue extends PlayerQueue
{
	private final Object waitLock = new Object();
	private SimplePlayerQueue.Entry waiting;
	
	public SimplePlayerQueue(MatchManager matchMgr)
	{
		super(matchMgr);
	}

	@Override
	public PlayerQueue.Entry enqueue(Session session)
	{
		Session opponent = null;
		synchronized(this.waitLock)
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
		
		// Create a new match between these players
		MatchPlayer p1 = new MatchPlayer(opponent);
		MatchPlayer p2 = new MatchPlayer(session);	
		this.getMatchMgr().newMatch(p1, p2);
		
		// Queue empty
		return null;
	}

	@Override
	public void dequeue(PlayerQueue.Entry entry)
	{
		synchronized(this.waitLock)
		{
			if(entry == this.waiting)
			{
				this.waiting = null;
			}
		}
	}
	
	@Override
	public Iterator<PlayerQueue.Entry> iterator()
	{
		return new Iterator<PlayerQueue.Entry>()
		{
			@Override
			public boolean hasNext()
			{
				return (waiting != null);
			}

			@Override
			public PlayerQueue.Entry next()
			{
				return waiting;
			}

			@Override
			public void remove()
			{
				if(waiting != null)
				{
					waiting.exit();
				}
			}
		};
	}
	
	private static class Entry extends PlayerQueue.Entry
	{
		public Entry(Session session, PlayerQueue queue)
		{
			super(session, queue);
		}
	}
}
