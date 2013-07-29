package strikd.game.match;

import strikd.game.user.User;
import strikd.sessions.Session;

public class MatchPlayer
{
	private final Session session;
	
	private Match match;
	private int actorId;
	private MatchTimer extraTimer;
	
	public MatchPlayer(Session session)
	{
		this.session = session;
	}

	public int getActorId()
	{
		return this.actorId;
	}
	
	public Session getSession()
	{
		return this.session;
	}
	
	public User getInfo()
	{
		return this.session.getUser();
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
