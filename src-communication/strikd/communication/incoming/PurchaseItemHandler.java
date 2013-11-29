package strikd.communication.incoming;

import java.util.List;

import strikd.sessions.Session;
import strikd.communication.Opcodes;
import strikd.communication.outgoing.AlertMessage;
import strikd.communication.outgoing.CurrencyBalanceMessage;
import strikd.communication.outgoing.ItemsAddedMessage;
import strikd.game.items.ItemInstance;
import strikd.game.items.shop.Shop;
import strikd.net.codec.IncomingMessage;

public class PurchaseItemHandler extends MessageHandler
{
	@Override
	public Opcodes.Incoming getOpcode()
	{
		return Opcodes.Incoming.PURCHASE_ITEM;
	}
	
	@Override
	public void handle(Session session, IncomingMessage request)
	{
		// Determine what offer to purchase
		int offerId = request.readInt();
		
		// Attempt to purchase the offer
		Shop shop = session.getServer().getShop();
		List<ItemInstance> items = shop.purchaseOffer(offerId, session.getPlayer());
		
		// Purchased successfully?
		if(items == null)
		{
			session.send(new AlertMessage("Purchase failed! You have not been charged."));
		}
		else
		{
			// Save immediately
			session.saveData();
			
			// You have been charged!
			session.send(new CurrencyBalanceMessage(session.getPlayer().getBalance()));
			
			// Add items
			for(ItemInstance item : items)
			{
				session.getPlayer().getItems().add(item);
			}
			session.send(new ItemsAddedMessage(items));
		}
	}
}
