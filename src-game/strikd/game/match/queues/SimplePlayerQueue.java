package strikd.game.match.queues;

import java.util.Iterator;

import strikd.game.match.MatchManager;
import strikd.game.match.MatchPlayer;
import strikd.locale.LocaleBundle;
import strikd.sessions.Session;

public class SimplePlayerQueue extends PlayerQueue
{
	private final Object waitLock = new Object();
	private SimplePlayerQueue.Entry waiting;
	private int lastWaitingTime;
	
	public SimplePlayerQueue(LocaleBundle locale, MatchManager matchMgr)
	{
		super(locale, matchMgr);
	}

	@Override
	public PlayerQueue.Entry enqueue(Session session)
	{
		Session opponent = null;
		synchronized(this.waitLock)
		{
			if(this.waiting == null)
			{
				PlayerQueue.Entry entry = new PlayerQueue.Entry(session, this);
				this.waiting = entry;
				
				return entry;
			}
			else
			{
				opponent = this.waiting.getSession();
				this.lastWaitingTime = this.waiting.getWaitingSeconds();
				this.waiting = null;
			}
		}
		
		// Create a new match between these players
		MatchPlayer p1 = new MatchPlayer(opponent);
		MatchPlayer p2 = new MatchPlayer(session);	
		this.newMatch(p1, p2);
		
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
	public int getAvgWaitingTime()
	{
		return this.lastWaitingTime;
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
}
