package strikd.communication.incoming;

import strikd.communication.Opcodes;
import strikd.communication.outgoing.InAppPurchaseProductsMessage;
import strikd.game.items.shop.Shop;
import strikd.net.codec.IncomingMessage;
import strikd.sessions.Session;

public class GetInAppPurchaseProductsHandler extends MessageHandler
{
	@Override
	public Opcodes.Incoming getOpcode()
	{
		return Opcodes.Incoming.GET_IN_APP_PURCHASE_PRODUCTS;
	}
	
	@Override
	public void handle(Session session, IncomingMessage request)
	{
		Shop shop = session.getServer().getShop();

		session.send(new InAppPurchaseProductsMessage(shop.getInAppPurchaseProductIds()));
	}
}
