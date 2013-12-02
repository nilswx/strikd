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
import strikd.sessions.Session;
import strikd.util.RandomUtil;

public class PlayerRegister extends Server.Referent
{
	private static final Logger logger = LoggerFactory.getLogger(PlayerRegister.class);
	
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
		player.setName(this.getDefaultName());
		player.setAvatar(this.getDefaultAvatar());
		player.setMotto(this.getDefaultMotto());
		player.setLocale(""); // Player will send CHANGE_LOCALE
		player.setCountry(""); // Will change after LOGIN
		player.setPlatform(""); // Will change after LOGIN
		player.setBalance(this.getDefaultBalance());
		player.setJoined(new Date());
		
		// Save to database
		this.getDatabase().save(player);
		logger.debug("created player {}", player);
		
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
	
	private String generateToken()
	{
		return (UUID.randomUUID().toString()).replace("-", "").toUpperCase();
	}
	
	public String getDefaultName()
	{
		return String.format("Player-%d", RandomUtil.pickInt(100000, 999999));
	}
	
	public String getDefaultAvatar()
	{
		return "ht1.hd2.ey4";
	}
	
	public String getDefaultMotto()
	{
		return "Hey I'm new!";
	}
	
	public int getDefaultBalance()
	{
		return 5;
	}
}
