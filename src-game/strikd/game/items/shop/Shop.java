package strikd.game.items.shop;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import strikd.Server;
import strikd.game.items.ItemInstance;
import strikd.game.items.ItemManager;
import strikd.game.items.ItemType;
import strikd.game.player.Player;

public class Shop extends Server.Referent
{
	private static final Logger logger = Logger.getLogger(Shop.class);
	
	private final ItemManager itemMgr;
	private final Map<Integer, ShopOffer> offers = Maps.newHashMap();
	
	public Shop(Server server, ItemManager itemMgr)
	{
		super(server);
		this.itemMgr = itemMgr;
		
		// Load offers
		for(ShopOffer offer : server.getDbCluster().getCollection("shop").find().as(ShopOffer.class))
		{
			this.offers.put(offer.offerId, offer);
		}
		logger.info(String.format("%d available offers", this.offers.size()));
	}
	
	public List<ItemInstance> purchaseOffer(int offerId, Player player)
	{
		// Unknown offer?
		ShopOffer offer = this.offers.get(offerId);
		if(offer == null)
		{
			logger.warn(String.format("%s tried to purchase unknown offer #%d", player, offerId));
			return null;
		}
		
		// Enough coins?
		if(player.balance < offer.price)
		{
			logger.warn(String.format("%s tried to purchase too expensive offer #%d", player, offerId));
			return null;
		}
		
		// Create instances
		List<ItemInstance> items = Lists.newArrayList();
		for(ShopProduct product : offer.products)
		{
			ItemType type = this.itemMgr.getItemType(product.itemTypeId);
			for(int i = 0; i < product.quantity; i++)
			{
				items.add(type.newInstance());
			}
		}
		
		// TODO: write transaction log for player (and statistics)
		logger.info(String.format("%s purchased offer #%d for %d coins", player, offerId, offer.price));
		
		return items;
	}
}
