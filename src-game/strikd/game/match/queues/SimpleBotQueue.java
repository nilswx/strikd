package strikd.game.match.queues;

import java.util.Collections;
import java.util.Iterator;

import strikd.game.match.MatchManager;
import strikd.game.match.MatchPlayer;
import strikd.game.match.bots.MatchBotFactory;
import strikd.locale.LocaleBundle;
import strikd.sessions.Session;

public class SimpleBotQueue extends PlayerQueue
{
	private MatchBotFactory botFactory = new MatchBotFactory();
	
	public SimpleBotQueue(LocaleBundle locale, MatchManager matchMgr)
	{
		super(locale, matchMgr);
	}

	@Override
	public PlayerQueue.Entry enqueue(Session session)
	{
		MatchPlayer player = new MatchPlayer(session);
		MatchPlayer bot = this.botFactory.newBotForOpponent(player);
		this.newMatch(player, bot);
		
		// Doesn't return entries, creates matches straight away
		return null;
	}

	@Override
	public void dequeue(PlayerQueue.Entry entry)
	{
		throw new UnsupportedOperationException("this type has no backing queue");
	}
	
	@Override
	public Iterator<PlayerQueue.Entry> iterator()
	{
		return Collections.emptyListIterator();
	}

	@Override
	public int getAvgWaitingTime()
	{
		// No waiting!
		return 0;
	}
}
