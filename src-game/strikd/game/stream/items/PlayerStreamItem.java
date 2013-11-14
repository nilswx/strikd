package strikd.game.stream.items;

import org.bson.types.ObjectId;

import strikd.game.stream.EventStreamItem;

public abstract class PlayerStreamItem extends EventStreamItem
{
	public ObjectId playerId;
	public String playerName;
}
