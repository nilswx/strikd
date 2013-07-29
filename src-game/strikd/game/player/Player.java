package strikd.game.player;

import java.util.Date;
import java.util.Map;

import org.bson.types.ObjectId;
import org.jongo.marshall.jackson.oid.Id;

import strikd.game.items.Item;

public class Player
{
	@Id
	public ObjectId id;
	public String token;
	public String email;
	
	public String name;
	
	// Stats
	public int xp;
	public int matches;
	public int wins;
	
	public int currency;
	public String language;
	public Avatar avatar;
	public Map<Integer, Item> items;
	
	public FacebookIdentity fbIdentity;
	
	public Date getJoinTime()
	{
		return new Date(this.id.getTime());
	}
	
	public int getLosses()
	{
		return (this.matches - this.wins);
	}
	
	@Override
	public String toString()
	{
		return String.format("#%s ('%s')", this.id, this.name);
	}
}
