package strikd.game.match;

import java.util.Map;
import java.util.HashMap;

import org.apache.log4j.Logger;

import strikd.ServerInstance;
import strikd.game.match.queues.PlayerQueue;
import strikd.game.match.queues.SimplePlayerQueue;
import strikd.locale.LocaleBundle;
import strikd.sessions.Session;

public class MatchMaker
{
	private static final Logger logger = Logger.getLogger(MatchMaker.class);
	
	private final Map<String, PlayerQueue> queues = new HashMap<String, PlayerQueue>(); 
	
	public MatchMaker(ServerInstance instance)
	{
		for(LocaleBundle locale : instance.getLocaleMgr().getBundles())
		{
			PlayerQueue queue = new SimplePlayerQueue(this);
			this.queues.put(locale.getLocale(), queue);
			
			logger.debug(String.format("created %s for %s", queue.getClass().getSimpleName(), locale.getLocale()));
		}
	}
	
	private PlayerQueue getQueue(Session session)
	{
		if(session.isLoggedIn())
		{
			return this.queues.get(session.getPlayer().language);
		}
		else
		{
			return null;
		}
	}
	
	public PlayerQueue.Entry requestMatch(Session session)
	{
		PlayerQueue queue = this.getQueue(session);
		if(queue != null)
		{
			PlayerQueue.Entry entry = queue.enqueue(session);
			if(entry != null)
			{
				return entry;
			}
		}
		
		return null;
	}

	public void newMatch(MatchPlayer... players)
	{
		
	}
}
