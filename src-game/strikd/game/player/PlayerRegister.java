package strikd.game.player;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.jongo.MongoCollection;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;

import strikd.Server;
import strikd.facebook.FacebookIdentity;
import strikd.sessions.Session;
import strikd.util.RandomUtil;

public class PlayerRegister extends Server.Referent
{
	private static final int FRIENDLIST_CACHE_EXPIRE_MINUTES = 15;
	
	private static final Logger logger = Logger.getLogger(PlayerRegister.class);
	
	private final MongoCollection dbPlayers;
	
	private final Cache<Long, List<ObjectId>> friendListCache =
			CacheBuilder.newBuilder()
			.expireAfterAccess(FRIENDLIST_CACHE_EXPIRE_MINUTES, TimeUnit.MINUTES).build();
	
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
	
	public List<ObjectId> getFriends(FacebookIdentity identity)
	{
		// In cache already?
		List<ObjectId> friendIds = this.friendListCache.getIfPresent(identity.userId);
		if(friendIds == null)
		{
			try
			{
				// Get all friendID's
				List<String> userIds = identity.getAPI().friendOperations().getFriendIds();
			
				// Find all players
				friendIds = Lists.newArrayList(this.dbPlayers.find("{'fb.userId':{$in:#}}", userIds).as(ObjectId.class));
				if(!friendIds.isEmpty())
				{
					this.friendListCache.put(identity.userId, friendIds);
				}
			}
			catch(Exception e)
			{
				logger.error(String.format("could not retrieve friend players for %s", identity), e);
				friendIds = Collections.emptyList();
			}
		}
		
		return friendIds;
	}
	
	public PlayerProfile getProfile(ObjectId playerId)
	{
		return this.dbPlayers.findOne(playerId)
				//.projection(...)
				.as(PlayerProfile.class);
	}
	
	public String generateDefaultName()
	{
		return String.format("Player-%d", RandomUtil.pickInt(100000, 999999));
	}
}
