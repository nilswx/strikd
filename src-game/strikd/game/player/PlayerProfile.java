package strikd.game.player;

import java.util.Date;

import org.bson.types.ObjectId;
import org.jongo.marshall.jackson.oid.Id;

public class PlayerProfile
{
	@Id
	public ObjectId playerId;
	public String name;
	public Avatar avatar;
	
	public int xp;
	public int level;
	
	public int matches;
	public int wins;
	public int losses;
	
	public Date lastOnline;
}
