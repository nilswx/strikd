package strikd.game.player;

import java.util.Random;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.jongo.MongoCollection;

import strikd.ServerInstance;
import strikd.sessions.Session;

public class PlayerRegister
{
	private static final Logger logger = Logger.getLogger(PlayerRegister.class);
	
	private final ServerInstance instance;
	private final MongoCollection dbPlayers;
	
	public PlayerRegister(ServerInstance instance)
	{
		this.instance = instance;
		this.dbPlayers = instance.getDbCluster().getCollection("players");
	}
	
	public Player newPlayer()
	{
		// Create new player with default data
		Player player = new Player();
		player.token = UUID.randomUUID().toString();
		player.name = this.generateGuestName();
		this.dbPlayers.save(player);
		
		logger.debug(String.format("created player %s", player));
		
		return player;
	}
	
	public void savePlayer(Player player)
	{
		this.dbPlayers.save(player);
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
	
	public String generateGuestName()
	{
		Random rand = new Random();
		
		return "guest" + (rand.nextInt(9999 - 100 + 1) + 100);
	}
}
