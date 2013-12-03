package strikd.game.player;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.avaje.ebean.SqlRow;
import com.google.common.collect.Maps;

import strikd.Server;
import strikd.game.stream.activity.PlayerJoinedStreamItem;
import strikd.sessions.Session;

public class PlayerRegister extends Server.Referent
{
	private static final Logger logger = LoggerFactory.getLogger(PlayerRegister.class);
	
	private final PlayerDefaults defaults = new PlayerDefaults();
	
	public PlayerRegister(Server server)
	{
		super(server);
		
		logger.info("{} players", this.getDatabase().find(Player.class).findRowCount());
	}
	
	public Player newPlayer()
	{
		// Create new player with default data
		Player player = this.getDatabase().createEntityBean(Player.class);
		player.setToken(this.generateToken());
		player.setName(this.defaults.generateName());
		player.setMotto(this.defaults.getMotto());
		player.setLevel(1); // Start at lvl 1
		player.setLocale(""); // Player will send CHANGE_LOCALE
		player.setCountry(""); // Will change after LOGIN
		player.setPlatform(""); // Will change after LOGIN
		player.setBalance(this.defaults.getBalance());
		player.setJoined(new Date());
		
		// Default inventory and avatar
		this.defaults.stockInventory(player);
		this.defaults.getAvatar().generateDefaultAvatar(player);
		
		// Save to database
		this.getDatabase().save(player);
		logger.debug("created player {}", player);
		
		// Hello world!
		PlayerJoinedStreamItem pj = new PlayerJoinedStreamItem();
		pj.setPlayer(player);
		this.getServer().getActivityStream().postItem(pj);
		
		return player;
	}
	
	public void savePlayer(Player player)
	{
		logger.debug("saving player {}", player);
		
		this.getDatabase().update(player);
	}
	
	public void deletePlayer(Player player)
	{
		logger.info("deleting player {}", player);
		
		this.getDatabase().delete(player);
	}
	
	public Player findPlayer(int playerId)
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
	
	public List<Player> getPlayers(List<Integer> playerIds)
	{
		return this.getDatabase().createQuery(Player.class).where().in("id", playerIds).findList();
	}
	
	public Map<Integer, Long> getFacebookMapping(List<Long> userIds)
	{
		Map<Integer, Long> mapping = Maps.newHashMapWithExpectedSize(userIds.size());
		for(SqlRow row : this.getDatabase().createSqlQuery("select id,fb_uid from players where fb_uid in(:ids)").setParameter("ids", userIds).findList())
		{
			mapping.put(row.getInteger("id"), row.getLong("fb_uid"));
		}
		
		return mapping;
	}
	
	public String generateToken()
	{
		return (UUID.randomUUID().toString()).replace("-", "").toUpperCase();
	}
	
	public PlayerDefaults getDefaults()
	{
		return this.defaults;
	}
}
