package strikd.communication.incoming;

import strikd.sessions.Session;
import strikd.communication.Opcodes;
import strikd.game.items.shop.Shop;
import strikd.net.codec.IncomingMessage;

public class PurchaseOfferHandler extends MessageHandler
{
	@Override
	public Opcodes.Incoming getOpcode()
	{
		return Opcodes.Incoming.PURCHASE_OFFER;
	}
	
	@Override
	public void handle(Session session, IncomingMessage request)
	{
		// Determine what offer to purchase
		int offerId = request.readInt();
		
		// Attempt to purchase the offer
		Shop shop = session.getServer().getShop();
		if(!shop.purchaseOffer(session, offerId))
		{
			session.sendAlert("Purchase failed! You have not been charged.");
		}
	}
}
