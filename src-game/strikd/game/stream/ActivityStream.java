package strikd.game.stream;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import strikd.Server;
import strikd.game.player.Player;
import strikd.game.stream.activity.ActivityStreamItem;

public class ActivityStream extends Server.Referent
{
	private static final Logger logger = LoggerFactory.getLogger(ActivityStream.class);

	public ActivityStream(Server server)
	{
		super(server);
		logger.info("{} events", server.getDatabase().find(ActivityStreamItem.class).findRowCount());
	}

	public void postItem(ActivityStreamItem item)
	{
		item.setTimestamp(new Date());
		this.getDatabase().insert(item);
		
		logger.debug("posted {} for {}", item, item.getPlayer());
	}

	public List<ActivityStreamItem> getPlayerStream(Player player, int start, int amount)
	{
		// Use pure SQL?
		/*
		for(SqlRow row : this.getDatabase()
			.createSqlQuery("select s.type,s.timestamp,p.id,p.name,p.avatar,f.name AS rname from stream s join players p on p.id=s.player_id join facebook f on f.user_id=p.facebook_user_id where id=" + player.getId())
			.findList())
		{
			System.out.println(row);
		}*/
		
		// Find items
		List<ActivityStreamItem> items = this.getDatabase().createQuery(ActivityStreamItem.class)
				
			// Read only!
			.setReadOnly(true)
	
			// Join partial player data
			.fetch("player", "id,name,avatar")
				
			// Last first
			.orderBy().desc("timestamp")
	
			// Add criteria
			.where().eq("player.id", player.getId())
			
			// TODO: or loser.id is player ID
			// TODO: or player.id is in friendslist
	
			// Determine range to fetch
			.setFirstRow(start)
			.setMaxRows(amount)
	
			// Return as stream items
			.findList();
		
		// Done!
		return items;
	}
}
