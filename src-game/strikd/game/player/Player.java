package strikd.game.player;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.jongo.marshall.jackson.oid.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;

import strikd.facebook.FacebookIdentity;
import strikd.game.items.ItemInstance;

public class Player
{
	@Id
	public ObjectId id;
	public String token;
	public String email;
	
	// Customization
	public String name;
	public Avatar avatar;
	public String language;
	
	// Clustering
	public int serverId;
	public ObjectId opponentId;
	
	// Ranking
	public int xp;
	public int level;
	
	// Stats
	public int logins;
	public int matches;
	public int wins;
	public int losses;
	public String platform;
	public Date lastOnline;
	
	// Inventory
	public int balance;
	public List<ItemInstance> items = new ArrayList<ItemInstance>();
	
	// Personal
	public String country;
	public FacebookIdentity fbIdentity;
	public boolean liked;
	
	public int getDraws()
	{
		return (this.matches - (this.wins - this.losses));
	}
	
	@JsonIgnore
	public boolean isFacebookLinked()
	{
		return (this.fbIdentity != null);
	}
	
	@JsonIgnore
	public boolean isOnline()
	{
		return (this.serverId > 0);
	}
	
	@JsonIgnore
	public boolean isInMatch()
	{
		return (this.serverId > 0 && this.opponentId != null);
	}
	
	@Override
	public String toString()
	{
		return String.format("#%s ('%s')", this.id, this.name);
	}

	public void updateLastOnline()
	{
		this.lastOnline = new Date();
	}
}
