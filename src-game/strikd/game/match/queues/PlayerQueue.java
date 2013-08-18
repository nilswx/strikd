package strikd.game.match.queues;

import strikd.game.match.Match;
import strikd.game.match.MatchManager;
import strikd.game.match.MatchPlayer;
import strikd.sessions.Session;

public abstract class PlayerQueue implements Iterable<PlayerQueue.Entry>
{
	private final String language;
	private final MatchManager matchMgr;
	
	protected PlayerQueue(String language, MatchManager matchMgr)
	{
		this.language = language;
		this.matchMgr = matchMgr;
	}
	
	public abstract PlayerQueue.Entry enqueue(Session session);
	
	public abstract void dequeue(Entry entry);
	
	protected Match newMatch(MatchPlayer... players)
	{
		return this.matchMgr.newMatch(this.language, players);
	}
	
	public String getLanguage()
	{
		return this.language;
	}
	
	public MatchManager getMatchMgr()
	{
		return this.matchMgr;
	}
	
	@Override
	public String toString()
	{
		return String.format("%s (%s)", this.getClass().getSimpleName(), this.language);
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