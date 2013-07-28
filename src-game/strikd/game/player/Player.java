package strikd.game.player;

import java.util.Date;
import java.util.Map;

import org.bson.types.ObjectId;

import strikd.game.items.Item;

public class Player
{
	public ObjectId id;
	public String name;
	
	// Stats
	public int xp;
	public int matches;
	public int wins;
	
	public int coins;
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
