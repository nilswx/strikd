package strikd.game.stream;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.jongo.MongoCollection;

import com.google.common.collect.Lists;

import strikd.Server;
import strikd.game.player.Player;
import strikd.game.stream.items.NewsStreamItem;

public class EventStreamManager extends Server.Referent
{
	private static final Logger logger = Logger.getLogger(EventStreamManager.class);
	
	private final MongoCollection dbNews, dbStream;
	private List<NewsStreamItem> news = Collections.emptyList();
	
	public EventStreamManager(Server server)
	{
		super(server);
		this.dbStream = server.getDbCluster().getCollection("stream");
		this.dbNews = server.getDbCluster().getCollection("news");
		
		logger.info(String.format("%d events", this.dbStream.count()));
	}
	
	public void reloadNews()
	{
		// Sample news item
		if(this.dbNews.count() == 0)
		{
			NewsStreamItem welcome = new NewsStreamItem();
			welcome.timestamp = new Date();
			welcome.headline = "Welcome aboard Strik!";
			welcome.imageUrl = "http://i.imgur.com/blabla.png";
			welcome.body = "Thanks for playing Strik! In the coming days we will blablablaa!";
			this.dbNews.save(welcome);
		}
		
		// Load them all into memory
		this.news = Lists.newArrayList(this.dbNews.find().as(NewsStreamItem.class));
		logger.info(String.format("%d news items", this.news.size()));
		
		// Print them
		for(NewsStreamItem news : this.news)
		{
			logger.debug(String.format("\"%s\" published on %s", news.headline, news.timestamp));
		}
	}
	
	public List<EventStreamItem> getPlayerStream(ObjectId playerId, long periodBegin, long periodEnd, Player requester)
	{
		//this.dbStream.find("{t:{$gte:#,$lt:#}}", periodBegin, periodEnd);
		
		// A list that will be sorted later
		List<EventStreamItem> result = Lists.newArrayList();
		
		// TODO: add the own items
		
		// TODO: add direct friend's items
		
		// Add the news items that are within range
		result.addAll(this.news);
		
		// TODO: sort on timestamp
		
		return result;
	}
}
