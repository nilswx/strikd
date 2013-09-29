package strikd.game.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.jongo.marshall.jackson.oid.Id;

import com.fasterxml.jackson.annotation.JsonFormat;

import strikd.game.items.Item;

public class User
{
	@Id
	public ObjectId id;
	public String token;
	public String email;
	
	public String name; // Allow users to purchase a name color?
	public Avatar avatar;
	public String country;
	
	// Stats
	public int logins;
	public int xp;
	public int matches;
	public int wins;
	
	public int balance;
	public String language;
	
	@JsonFormat(shape=JsonFormat.Shape.ARRAY)
	public List<Item> items = new ArrayList<Item>();
	
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
