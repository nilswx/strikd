package strikd.game.stream;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import strikd.Server;
import static com.avaje.ebean.Expr.in;

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

	public List<ActivityStreamItem> filterItems(List<Integer> relevantPlayers, int start, int amount)
	{
		// Use pure SQL for performance?
		/*
		for(SqlRow row : this.getDatabase()
			.createSqlQuery("select s.type,s.timestamp,p.id,p.name,p.avatar,f.name AS rname from stream s join players p on p.id=s.player_id join facebook f on f.user_id=p.facebook_user_id where id=" + player.getId())
			.findList())
		{
			System.out.println(row);
		}*/
		
		// Build query
		return this.getDatabase().createQuery(ActivityStreamItem.class)
				
				// Join partial player data
				.fetch("player", "id,name,avatar")
				.fetch("loser", "id,name,avatar")

				// Add criteria
				.where().or
				(
					in("player.id", relevantPlayers),
					in("loser.id", relevantPlayers)
				)
				
				// Last first
				.orderBy().desc("id")
				
				// Range to fetch (paging)
				.setFirstRow(start)
				.setMaxRows(amount)
				
				// Fetch results
				.findList();
	}
}
