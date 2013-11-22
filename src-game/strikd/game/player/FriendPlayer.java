package strikd.game.player;

import javax.persistence.Embedded;
//import javax.persistence.Entity;
import javax.persistence.Id;
//import javax.persistence.Table;

import strikd.facebook.FacebookIdentity;

//@Entity @Table(name="players")
public class FriendPlayer
{
	@Id
	private long id;
	private String name;
	private String avatar;
	
	@Embedded
	private FacebookIdentity facebook;
	private int serverId;
	private long opponentId;
	
	public long getId()
	{
		return this.id;
	}
	
	public void setId(long id)
	{
		this.id = id;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getAvatar()
	{
		return this.avatar;
	}
	
	public void setAvatar(String avatar)
	{
		this.avatar = avatar;
	}
	
	public FacebookIdentity getFacebook()
	{
		return this.facebook;
	}
	
	public void setFacebook(FacebookIdentity facebook)
	{
		this.facebook = facebook;
	}
	
	public int getServerId()
	{
		return this.serverId;
	}
	
	public void setServerId(int serverId)
	{
		this.serverId = serverId;
	}
	
	public long getOpponentId()
	{
		return this.opponentId;
	}
	
	public void setOpponentId(long opponentId)
	{
		this.opponentId = opponentId;
	}
	
	public boolean isOnline()
	{
		return (this.getServerId() > 0);
	}
	
	public boolean isInMatch()
	{
		return (this.getOpponentId() > 0);
	}
}
