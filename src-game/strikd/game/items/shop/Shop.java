package strikd.game.items.shop;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import strikd.Server;
import strikd.game.items.ItemInstance;
import strikd.game.player.Player;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class Shop extends Server.Referent
{
	private static final Logger logger = LoggerFactory.getLogger(Shop.class);
	
	private final Map<Integer, ShopOffer> offers = Maps.newHashMap();
	
	public Shop(Server server)
	{
		super(server);
		
		// Load offers
		/*
		for(ShopOffer offer : server.getDbCluster().getCollection("shop").find().as(ShopOffer.class))
		{
			this.offers.put(offer.offerId, offer);
		}
		logger.info("%d available offers", this.offers.size()));*/
	}
	
	public List<ItemInstance> purchaseOffer(int offerId, Player player)
	{
		// Unknown offer?
		ShopOffer offer = this.offers.get(offerId);
		if(offer == null)
		{
			logger.warn("{} tried to purchase unknown offer #{}", player, offerId);
			return null;
		}
		
		// Enough coins?
		if(player.getBalance() < offer.price)
		{
			logger.warn("{} tried to purchase too expensive offer #{}", player, offerId);
			return null;
		}
		
		// Create instances
		List<ItemInstance> items = Lists.newArrayList();
		for(ShopProduct product : offer.products)
		{
			for(int i = 0; i < product.quantity; i++)
			{
				items.add(product.type.newInstance());
			}
		}
		
		// TODO: write transaction log for player (and statistics)
		logger.info("{} purchased offer #{} for {} coins", player, offerId, offer.price);
		
		return items;
	}
}
