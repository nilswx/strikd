package strikd.communication.incoming;

import strikd.sessions.Session;
import strikd.communication.Opcodes;
import strikd.game.items.shop.Shop;
import strikd.game.util.Platform;
import strikd.net.codec.IncomingMessage;

public class RedeemInAppPurchaseHandler extends MessageHandler
{
	@Override
	public Opcodes.Incoming getOpcode()
	{
		return Opcodes.Incoming.REDEEM_IN_APP_PURCHASE;
	}
	
	@Override
	public void handle(Session session, IncomingMessage request)
	{
		// Retrieve shop
		Shop shop = session.getServer().getShop();
		
		// This logic is platform specific!
		if(session.getPlayer().getPlatform() == Platform.IOS)
		{
			String receipt = request.readStr();
			if(!shop.redeemAppStoreReceipt(session, receipt))
			{
				session.sendAlert("Invalid App Store receipt...");
			}
		}
		else
		{
			// Probably Android or something else, LOL
			session.sendAlert("Strik on '%s' does not support in-app purchases as of yet!",
					session.getPlayer().getPlatform());
		}
	}
}
