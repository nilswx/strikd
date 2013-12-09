package strikd.game.items.shop;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import strikd.Server;
import strikd.communication.outgoing.CurrencyBalanceMessage;
import strikd.communication.outgoing.ItemsAddedMessage;
import strikd.game.player.Player;
import strikd.sessions.Session;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class Shop extends Server.Referent
{
	private static final Logger logger = LoggerFactory.getLogger(Shop.class);
	
	private final Map<String, ShopPage> pages = Maps.newHashMap();
	private final Map<Integer, ShopOffer> offers = Maps.newHashMap();
	
	private final ShopPage coinsPage;
	
	public Shop(Server server)
	{
		super(server);
				
		// System pages
		this.coinsPage = this.getPage("COINS");
		this.pages.put("COINS", this.coinsPage);
		this.pages.put("POWERUPS", new ShopPage("POWERUPS"));
		this.pages.put("PARTS", new ShopPage("PARTS"));
		
		// TODO: load offers from db
		
		// Print shop
		logger.info("pages = {}", this.pages.values());
	}
	
	public boolean purchaseOffer(Session session, int offerId)
	{
		// Get player
		Player player = session.getPlayer();
		if(player == null)
		{
			return false;
		}
		
		// Unknown offer?
		ShopOffer offer = this.offers.get(offerId);
		if(offer == null || this.coinsPage.getOffers().contains(offer))
		{
			logger.warn("{} tried to purchase unknown offer #{}", player, offerId);
			return false;
		}
		
		// Enough coins?
		if(player.getBalance() < offer.getPrice())
		{
			logger.warn("{} tried to purchase too expensive offer #{}", player, offerId);
			return false;
		}
		
		// Charge player
		player.setBalance(player.getBalance() - offer.getPrice());
		session.send(new CurrencyBalanceMessage(player.getBalance()));
		
		// Deliver products
		this.deliverProducts(session, offer);
		
		// Save all data
		session.saveData();
		
		// TODO: write transaction log for player (and statistics)
		logger.info("{} purchased offer #{} for {} coins", player, offerId, offer.getPrice());
		
		return true;
	}
	
	private void deliverProducts(Session session, ShopOffer offer)
	{
		Player player = session.getPlayer();
		
		// Add coins and items
		List<ShopProduct> addedItems = Lists.newArrayListWithCapacity(offer.getProducts().size());
		for(ShopProduct product : offer.getProducts())
		{
			if(product.getItem() instanceof Coin)
			{
				player.setBalance(player.getBalance() + product.getQuantity());
				session.send(new CurrencyBalanceMessage(player.getBalance()));
			}
			else
			{
				player.getInventory().add(product.getItem(), product.getQuantity());
				addedItems.add(product);
			}
		}
		player.saveInventory();
		
		// Send added items
		if(!addedItems.isEmpty())
		{
			session.send(new ItemsAddedMessage(addedItems));
		}
	}
	
	public boolean purchaseWithAppStoreReceipt(Session session, String receipt)
	{
		// TODO: check receipt at Apple's servers
		
		// TODO: check in own DB if receipt has been used before, flag 'used'
		
		// TODO: parse purchase identifier
		
		// TODO: resolve matching offer
		
		// TODO: deliver products from offer
		
		// Save data
		session.saveData();
		
		return false;
	}
	
	public ShopPage getPage(String pageId)
	{
		return this.pages.get(pageId);
	}
}
