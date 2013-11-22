package strikd.game.player;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import strikd.facebook.FacebookIdentity;
import strikd.game.items.ItemInstance;

@Entity @Table(name="players")
public class Player
{
	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(nullable=false)
	private String token;
	
	@Column(length=32,nullable=false)
	private String name;
	
	@Column(nullable=false)
	private Date joined;
	
	private String avatar;
	
	private String language;
	
	private String country;
	
	private String platform;
	
	@Column(nullable=false)
	private int serverId;
	
	private long opponentId;
	
	@Column(nullable=false)
	private int xp;
	
	@Column(nullable=false)
	private int level;
	
	@Column(nullable=false)
	private int logins;
	
	@Column(nullable=false)
	private int matches;
	
	@Column(nullable=false)
	private int wins;
	
	@Column(nullable=false)
	private int losses;
	
	@Column(nullable=false)
	private int balance;
	
	@Transient
	private List<ItemInstance> items = new ArrayList<ItemInstance>();
	
	@Embedded
	private FacebookIdentity facebook;
	
	@Column(nullable=false)
	private boolean liked;
	
	@Version
	@Column(nullable=false)
	private Date lastUpdate;
	
	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public String getToken()
	{
		return token;
	}

	public void setToken(String token)
	{
		this.token = token;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getAvatar()
	{
		return avatar;
	}

	public void setAvatar(String avatar)
	{
		this.avatar = avatar;
	}

	public String getLanguage()
	{
		return language;
	}

	public void setLanguage(String language)
	{
		this.language = language;
	}

	public int getServerId()
	{
		return serverId;
	}

	public void setServerId(int serverId)
	{
		this.serverId = serverId;
	}

	public long getOpponentId()
	{
		return opponentId;
	}

	public void setOpponentId(long opponentId)
	{
		this.opponentId = opponentId;
	}

	public int getXp()
	{
		return xp;
	}

	public void setXp(int xp)
	{
		this.xp = xp;
	}

	public int getLevel()
	{
		return level;
	}

	public void setLevel(int level)
	{
		this.level = level;
	}

	public int getLogins()
	{
		return logins;
	}

	public void setLogins(int logins)
	{
		this.logins = logins;
	}

	public int getMatches()
	{
		return matches;
	}

	public void setMatches(int matches)
	{
		this.matches = matches;
	}

	public int getWins()
	{
		return wins;
	}

	public void setWins(int wins)
	{
		this.wins = wins;
	}

	public int getLosses()
	{
		return losses;
	}

	public void setLosses(int losses)
	{
		this.losses = losses;
	}

	public String getPlatform()
	{
		return platform;
	}

	public void setPlatform(String platform)
	{
		this.platform = platform;
	}
	
	public int getBalance()
	{
		return balance;
	}

	public void setBalance(int balance)
	{
		this.balance = balance;
	}

	public List<ItemInstance> getItems()
	{
		return items;
	}

	public void setItems(List<ItemInstance> items)
	{
		this.items = items;
	}

	public String getCountry()
	{
		return country;
	}

	public void setCountry(String country)
	{
		this.country = country;
	}
	
	public boolean isLiked()
	{
		return liked;
	}

	public void setLiked(boolean liked)
	{
		this.liked = liked;
	}
	
	public Date getJoined()
	{
		return joined;
	}

	public void setJoined(Date joined)
	{
		this.joined = joined;
	}

	public Date getLastUpdate()
	{
		return lastUpdate;
	}
	
	public void setLastUpdate(Date lastUpdate)
	{
		this.lastUpdate = lastUpdate;
	}

	public int getDraws()
	{
		return (this.matches - (this.wins - this.losses));
	}
	
	public boolean isFacebookLinked()
	{
		return (this.facebook != null);
	}
	
	public FacebookIdentity getFacebook()
	{
		return facebook;
	}

	public void setFacebook(FacebookIdentity fbIdentity)
	{
		this.facebook = fbIdentity;
	}

	public boolean isOnline()
	{
		return (this.serverId > 0);
	}
	
	public boolean isInMatch()
	{
		return (this.serverId > 0 && this.opponentId > 0);
	}
	
	@Override
	public String toString()
	{
		return String.format("#%s ('%s')", this.id, this.name);
	}
}
