package strikd.game.match;

import strikd.game.user.User;
import strikd.net.codec.OutgoingMessage;
import strikd.sessions.Session;

public class MatchPlayer
{
	private final Session session;
	
	private Match match;
	private int actorId;
	private boolean ready;
	
	public MatchPlayer(Session session)
	{
		this.session = session;
	}
	
	public void send(OutgoingMessage msg)
	{
		this.session.send(msg);
	}
		
	public void setMatch(int actorId, Match match)
	{
		this.actorId = actorId;
		this.match = match;
		if(this.session != null)
		{
			this.session.setMatchPlayer(this);
		}
	}
	
	public void leave()
	{
		this.match.removePlayer(this);
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
	
	public int getActorId()
	{
		return this.actorId;
	}
	
	public void setReady()
	{
		this.ready = true;
		if(this.match != null)
		{
			this.match.checkReady();
		}
	}
	
	public boolean isReady()
	{
		return this.ready;
	}
}
