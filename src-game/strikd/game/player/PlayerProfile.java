package strikd.game.player;

import org.bson.types.ObjectId;

public class PlayerProfile
{
	public ObjectId playerId;
	public String playerName;
	public Avatar avatar;
	public int xp;
	public int level;
}
