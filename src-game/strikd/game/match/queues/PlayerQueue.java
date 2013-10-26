package strikd.game.match.queues;

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
		return this.matchMgr.newMatch(this.locale, playerOne, playerTwo);
	}
	
	public LocaleBundle getLocale()
	{
		return this.locale;
	}
	
	public MatchManager getMatchMgr()
	{
		return this.matchMgr;
	}
	
	@Override
	public String toString()
	{
		return String.format("%s (%s)", this.getClass().getSimpleName(), this.locale);
	}
	
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