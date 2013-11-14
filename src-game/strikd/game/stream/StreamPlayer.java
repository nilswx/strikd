package strikd.game.stream;

import org.bson.types.ObjectId;

import strikd.game.player.Avatar;

public class StreamPlayer
{
	public static final StreamPlayer SELF = null;
	
	public ObjectId playerId;
	public String realName;
	public Avatar avatar;
}
