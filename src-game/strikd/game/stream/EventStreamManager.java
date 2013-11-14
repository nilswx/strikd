package strikd.game.stream;

import java.util.List;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;

import strikd.Server;
import strikd.game.player.Player;

public class EventStreamManager extends Server.Referent
{
	private static final Logger logger = Logger.getLogger(EventStreamManager.class);
	
	public EventStreamManager(Server server)
	{
		super(server);
		logger.info("initialized!");
	}
	
	public List<EventStreamItem> getPlayerStream(ObjectId playerId, Player forPlayer)
	{
		return null;
	}
}
