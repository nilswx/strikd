package strikd.game.items;

import java.util.Map;

import org.apache.log4j.Logger;

import com.google.common.collect.Maps;

import strikd.Server;

public class ItemManager extends Server.Referent
{
	private static final Logger logger = Logger.getLogger(ItemManager.class);
	
	private final Map<Integer, ItemType> types = Maps.newHashMap();
	
	public ItemManager(Server server)
	{
		super(server);
		
		// Load all item types
		for(ItemType type : server.getDbCluster().getCollection("items").find().as(ItemType.class))
		{
			this.types.put(type.typeId, type);
		}
		logger.info(String.format("%d known item types", this.types.size()));
	}
	
	public ItemType getItemType(int typeId)
	{
		return this.types.get(typeId);
	}
}
