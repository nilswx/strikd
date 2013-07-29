package strikd.game.match;

import strikd.game.player.Player;
import strikd.sessions.Session;

public class MatchPlayer
{
	private final Session session;
	private Match match;
	private MatchTimer extraTimer;
	
	public MatchPlayer(Session session)
	{
		this.session = session;
	}

	public Session getSession()
	{
		return this.session;
	}
	
	public Player getInfo()
	{
		return this.session.getPlayer();
	}
	
	public Match getMatch()
	{
		return this.match;
	}
	
	public MatchTimer getExtraTimer()
	{
		return this.extraTimer;
	}
}
