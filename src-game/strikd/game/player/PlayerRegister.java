package strikd.game.player;

import java.util.UUID;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.jongo.MongoCollection;

import strikd.Server;
import strikd.sessions.Session;
import strikd.util.RandomUtil;

public class PlayerRegister extends Server.Referent
{
	private static final Logger logger = Logger.getLogger(PlayerRegister.class);
	
	private final MongoCollection dbPlayers;
	
	public PlayerRegister(Server server)
	{
		super(server);
		this.dbPlayers = this.getServer().getDbCluster().getCollection("players");
		
		logger.info(String.format("%d players", this.dbPlayers.count()));
	}
	
	public Player newPlayer()
	{
		// Create new player with default data
		Player player = new Player();
		player.token = UUID.randomUUID().toString();
		player.name = this.generateDefaultName();
		player.language = "en_US";
		player.country = "nl"; // From FB
		player.balance = 5;
		this.dbPlayers.save(player);
		
		logger.debug(String.format("created player %s", player));
		
		return player;
	}
	
	public void savePlayer(Player player)
	{
		this.dbPlayers.save(player);
	}
	
	public Player findPlayer(ObjectId playerId)
	{
		Session session = this.getServer().getSessionMgr().getPlayerSession(playerId);
		if(session != null)
		{
			return session.getPlayer();
		}
		else
		{
			return this.dbPlayers.findOne(playerId).as(Player.class);
		}
	}
	
	public String generateDefaultName()
	{
		return String.format("Player-%d", RandomUtil.pickInt(100000, 999999));
	}
}
