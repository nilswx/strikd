package strikd.game.player;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import strikd.Server;
import strikd.facebook.FacebookIdentity;
import strikd.sessions.Session;
import strikd.util.RandomUtil;

import com.google.common.base.Function;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;

public class PlayerRegister extends Server.Referent
{
	private static final int FRIENDLIST_CACHE_EXPIRE_MINUTES = 15;
	
	private static final Logger logger = LoggerFactory.getLogger(PlayerRegister.class);
	
	private final Cache<Long, List<Long>> friendListCache =
			CacheBuilder.newBuilder()
			.expireAfterAccess(FRIENDLIST_CACHE_EXPIRE_MINUTES, TimeUnit.MINUTES).build();
	
	public PlayerRegister(Server server)
	{
		super(server);
		
		logger.info("{} players", this.getDatabase().find(Player.class).findRowCount());
	}
	
	public Player newPlayer()
	{
		// Create new player with default data
		Player player = this.getDatabase().createEntityBean(Player.class);
		player.setToken(UUID.randomUUID().toString().replace("-", "").toUpperCase());
		player.setJoined(new Date());
		player.setName(this.generateDefaultName());
		player.setLocale("en_US");
		player.setBalance(5);
		
		// Save to database
		this.getDatabase().save(player);
		logger.debug("created player {}", player);
		
		return player;
	}
	
	public void savePlayer(Player player)
	{
		this.getDatabase().update(player);
	}
	
	public Player findPlayer(long playerId)
	{
		Session session = this.getServer().getSessionMgr().getPlayerSession(playerId);
		if(session != null)
		{
			return session.getPlayer();
		}
		else
		{
			return this.getDatabase().find(Player.class, playerId);
		}
	}
	
	public List<Long> getFriends(FacebookIdentity identity)
	{
		// In cache already?
		List<Long> friendIds = this.friendListCache.getIfPresent(identity.getUserId());
		if(friendIds == null)
		{
			try
			{
				// Get and parse all friend IDs (these are stupid strings!)
				List<String> userStrIds = Collections.emptyList();//identity.getAPI().friendOperations().getFriendIds();
				List<Long> userIds = Lists.transform(userStrIds, new Function<String, Long>()
				{
					public Long apply(String e)
					{
						return Long.parseLong(e);
					}
				});
				userStrIds = null;
				
				// Find all players
				friendIds = Lists.newArrayList();//this.dbPlayers.find("{'fb.userId':{$in:#}}", userIds).projection("{_id:1}").as(ObjectId.class));
				if(!friendIds.isEmpty())
				{
					this.friendListCache.put(identity.getUserId(), friendIds);
				}
			}
			catch(Exception e)
			{
				logger.error("could not retrieve friend players for {}", identity, e);
				friendIds = Collections.emptyList();
			}
		}
		
		return friendIds;
	}
	
	public List<Player> getFriendPlayers(long... playerIds)
	{
		return this.getDatabase().createQuery(Player.class).where().in("id", playerIds).findList();
	}
	
	public PlayerProfile getProfile(long playerId)
	{
		return null;/*this.dbPlayers.findOne(playerId)
				//.projection(...)
				.as(PlayerProfile.class);*/
	}
	
	public String generateDefaultName()
	{
		return String.format("Player-%d", RandomUtil.pickInt(100000, 999999));
	}
}
