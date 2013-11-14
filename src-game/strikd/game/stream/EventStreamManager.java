package strikd.game.stream;

import java.util.List;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.jongo.MongoCollection;

import strikd.Server;
import strikd.game.player.Player;

public class EventStreamManager extends Server.Referent
{
	private static final Logger logger = Logger.getLogger(EventStreamManager.class);
	
	private final MongoCollection dbStream;
	
	public EventStreamManager(Server server)
	{
		super(server);
		this.dbStream = server.getDbCluster().getCollection("stream");
		
		logger.info(String.format("%d events", this.dbStream.count()));
	}
	
	public List<EventStreamItem> getPlayerStream(ObjectId playerId, long periodBegin, long periodEnd, Player forPlayer)
	{
		return null;
	}
}
