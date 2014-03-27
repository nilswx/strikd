package strikd.game.news;

import java.util.List;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import strikd.Server;
import strikd.communication.outgoing.NewsMessage;

public class NewsManager extends Server.Referent
{
	private static final Logger logger = LoggerFactory.getLogger(NewsManager.class);
	
	private List<NewsItem> items;
	private NewsMessage cachedMessage;
	
	public NewsManager(Server server)
	{
		super(server);
		this.reload();
	}
	
	public void reload()
	{
		// Needs seeding?
		if(this.getDatabase().find(NewsItem.class).findRowCount() == 0)
		{
			// Strik development started!
			NewsItem item = new NewsItem();
			item.setId(1);
			item.setHeadline("Development started!");
			item.setBody("There's a lot of stuff to be done... Stay tuned.");
			item.setImageUrl("http://cdn.strik.it/news/dev-started.png");
			item.setTimestamp(DateTime.parse("2013-07-23T15:00:00Z").toDate());
			this.getDatabase().insert(item);
			
			// XMAS!
			item = new NewsItem();
			item.setId(2);
			item.setHeadline("Happy Holidays!");
			item.setBody("Strik wishes you a Merry Christmas and a great 2014!");
			item.setImageUrl("http://cdn.strik.it/news/holidays-2013.png");
			item.setTimestamp(DateTime.parse("2013-12-20T13:00:00Z").toDate());
			this.getDatabase().insert(item);
		}
		
		// Load items and rebuild cache
		this.items = this.getDatabase().find(NewsItem.class).orderBy().desc("timestamp").findList();
		this.cachedMessage = new NewsMessage(this.items);
		
		// Done!
		logger.info("reloaded {} items (message={} bytes)", this.items.size(), this.cachedMessage.length());
	}
	
	public NewsMessage getCachedNewsMessage()
	{
		return this.cachedMessage;
	}
}
