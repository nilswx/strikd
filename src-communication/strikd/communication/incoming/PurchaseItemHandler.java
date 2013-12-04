package strikd.communication.incoming;

import strikd.sessions.Session;
import strikd.communication.Opcodes;
import strikd.communication.outgoing.CurrencyBalanceMessage;
import strikd.game.items.shop.Shop;
import strikd.game.player.Player;
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
		Player player = session.getPlayer();
		Object items = shop.purchaseOffer(offerId, player);
		
		// Purchased successfully?
		if(items == null)
		{
			session.sendAlert("Purchase failed! You have not been charged.");
		}
		else
		{
			// Save immediately
			session.saveData();
			
			// You have been charged!
			session.send(new CurrencyBalanceMessage(player.getBalance()));
			
			// Add items
		}
	}
}
