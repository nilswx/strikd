package strikd.game.player;

import java.util.Date;

import org.bson.types.ObjectId;

public class PlayerProfile
{
	public ObjectId playerId;
	public String playerName;
	public Avatar avatar;
	
	public int xp;
	public int level;
	
	public int matches;
	public int wins;
	public int losses;
	
	public Date lastOnline;
}
