package strikd.game.match.queues;

import java.util.Collections;
import java.util.Iterator;

import strikd.game.match.MatchBotFactory;
import strikd.game.match.MatchManager;
import strikd.game.match.MatchPlayer;
import strikd.sessions.Session;

public class SimpleBotQueue extends PlayerQueue
{
	private MatchBotFactory botFactory;
	
	public SimpleBotQueue(MatchManager matchMgr)
	{
		super(matchMgr);
		this.botFactory = new MatchBotFactory();
	}

	@Override
	public PlayerQueue.Entry enqueue(Session session)
	{
		MatchPlayer player = new MatchPlayer(session);
		MatchPlayer bot = this.botFactory.newBot();
		this.getMatchMgr().newMatch(player, bot);
		
		// Doesn't return entries, creates matches straight away
		return null;
	}

	@Override
	public void dequeue(PlayerQueue.Entry entry)
	{
		throw new java.lang.UnsupportedOperationException("this type has no backing queue");
	}
	
	@Override
	public Iterator<PlayerQueue.Entry> iterator()
	{
		return Collections.emptyListIterator();
	}
}
