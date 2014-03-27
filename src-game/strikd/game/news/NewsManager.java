package strikd.game.news;

import java.util.List;

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
