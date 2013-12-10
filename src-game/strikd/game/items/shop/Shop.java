package strikd.game.items.shop;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import strikd.Server;
import strikd.communication.outgoing.CurrencyBalanceMessage;
import strikd.communication.outgoing.InAppPurchaseDeliveredMessage;
import strikd.communication.outgoing.ItemsAddedMessage;
import strikd.game.player.Player;
import strikd.game.util.AppStoreReceipts;
import strikd.sessions.Session;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import static strikd.game.items.ItemTypeRegistry._I;

public class Shop extends Server.Referent
{
	private static final Logger logger = LoggerFactory.getLogger(Shop.class);
	
	private final Map<String, ShopPage> pages = Maps.newHashMap();
	private final Map<Integer, ShopOffer> offers = Maps.newHashMap();
	private final Map<String, ShopOffer> iapProducts = Maps.newHashMap();
	
	public Shop(Server server)
	{
		super(server);
				
		// System pages
		this.pages.put("COINS", new ShopPage("COINS"));
		this.pages.put("POWERUPS", new ShopPage("POWERUPS"));
		this.pages.put("PARTS", new ShopPage("PARTS"));
		
		// TODO: load offers from db
		
		// Register IAP's (universal identifier for all platforms)
		ShopOffer pouch = new ShopOffer(1337, 0);
		pouch.getProducts().add(new ShopProduct(_I("COIN"), 15));
		this.iapProducts.put("nl.indev.Strik.coins15", pouch);
		
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
		if(offer == null || this.iapProducts.containsValue(offer))
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
	
	public boolean redeemAppStoreReceipt(Session session, String receiptStr)
	{
		// Check receipt status at server
		JSONObject result = AppStoreReceipts.verifyReceipt(receiptStr, true);
		if(result.getInt("status") != 0)
		{
			return false;
		}
		
		// Get the actual receipt data
		JSONObject receipt = result.getJSONObject("receipt");
		
		// Look for the transaction in the DB
		long transactionId = receipt.getLong("transaction_id");
		InAppPurchaseTransaction transaction = this.getDatabase().find(InAppPurchaseTransaction.class, transactionId);
		
		// Used before?!
		if(transaction != null)
		{
			logger.warn("{} tried to redeem the already completed transaction #{} (by #{})",
					session.getPlayer(),
					transaction.getId(),
					transaction.getPlayer().getId());
			
			// Forget it!
			session.send(new InAppPurchaseDeliveredMessage(transactionId));
			return false;
		}
		
		// Resolve IAP's offer
		ShopOffer offer = this.iapProducts.get(receipt.getString("product_id"));
		if(offer == null)
		{
			return false;
		}
		
		// Go go!
		this.deliverProducts(session, offer);
		
		// Save data
		session.saveData();
		
		// Log transaction
		transaction = new InAppPurchaseTransaction(transactionId,session.getPlayer());
		this.getDatabase().insert(transaction);
		
		// Force client to complete the transaction
		session.send(new InAppPurchaseDeliveredMessage(transactionId));
		
		return true;
	}
	
	public ShopPage getPage(String pageId)
	{
		return this.pages.get(pageId);
	}
	
	public Set<String> getInAppPurchaseProductIds()
	{
		return this.iapProducts.keySet();
	}
}
