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
		player.setToken(UUID.randomUUID().toString().replace("-", "").toUpperCase());
		player.setJoined(new Date());
		player.setName(this.getDefaultName());
		player.setAvatar(this.getDefaultAvatar());
		player.setMotto(this.getDefaultMotto());
		player.setLocale(""); // Player will send CHANGE_LOCALE
		player.setCountry(""); // Will change after LOGIN
		player.setPlatform(""); // Will change after LOGIN
		player.setBalance(this.getDefaultBalance());
		
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
	
	public List<Player> getPlayers(List<Long> playerIds)
	{
		return this.getDatabase().createQuery(Player.class).where().in("id", playerIds).findList();
	}
	
	public Map<Long, Long> getFacebookMapping(List<Long> userIds)
	{
		Map<Long, Long> mapping = Maps.newHashMapWithExpectedSize(userIds.size());
		for(SqlRow row : this.getDatabase().createSqlQuery("select fb_uid,id from players where fb_uid in(:ids)").setParameter("ids", userIds).findList())
		{
			mapping.put(row.getLong("fb_uid"), row.getLong("id"));
		}
		
		return mapping;
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
