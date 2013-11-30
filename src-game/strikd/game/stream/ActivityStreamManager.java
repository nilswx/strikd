package strikd.game.stream;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import strikd.Server;
import strikd.game.player.Player;
import strikd.game.stream.activity.ActivityStreamItem;

public class ActivityStreamManager extends Server.Referent
{
	private static final Logger logger = LoggerFactory
			.getLogger(ActivityStreamManager.class);

	public ActivityStreamManager(Server server)
	{
		super(server);
		logger.info("{} events");
	}

	public void postItem(ActivityStreamItem item)
	{
		item.setTimestamp(new Date());
		this.getDatabase().insert(item);
		
		logger.debug("posted {} for {}", item, item.getPlayer());
	}

	public List<ActivityStreamItem> getPlayerStream(Player player, int start, int amount)
	{
		// Find items
		List<ActivityStreamItem> items = this.getDatabase().createQuery(ActivityStreamItem.class)
	
			// Join partial player data
			.fetch("player", "id,name,avatar")
				
			// Last first
			.orderBy().desc("timestamp")
	
			// Add criteria
			.where().eq("player.id", player.getId())
	
			// Determine range to fetch
			.setFirstRow(start)
			.setMaxRows(amount)
	
			// Return as stream items
			.findList();
		
		// Done!
		return items;
	}
}
