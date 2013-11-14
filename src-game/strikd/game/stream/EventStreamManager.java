package strikd.game.stream;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;

import strikd.Server;

public class EventStreamManager extends Server.Referent
{
	private static final Logger logger = Logger.getLogger(EventStreamManager.class);
	
	public EventStreamManager(Server server)
	{
		super(server);
		logger.info("initialized!");
	}
	
	public EventStream getPlayerStream(ObjectId playerId)
	{
		return null;
	}
}
