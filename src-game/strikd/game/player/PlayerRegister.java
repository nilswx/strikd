package strikd.game.player;

import org.bson.types.ObjectId;
import org.jongo.MongoCollection;

import strikd.ServerInstance;
import strikd.sessions.Session;

public class PlayerRegister
{
	private final ServerInstance instance;
	private final MongoCollection dbPlayers;
	
	public PlayerRegister(ServerInstance instance)
	{
		this.instance = instance;
		this.dbPlayers = instance.getDbCluster().getCollection("players");
	}
	
	public Player getPlayer(ObjectId playerId)
	{
		Session session = this.instance.getSessionMgr().getPlayerSession(playerId);
		if(session != null)
		{
			return session.getPlayer();
		}
		else
		{
			return this.dbPlayers.findOne(playerId).as(Player.class);
		}
	}
}
