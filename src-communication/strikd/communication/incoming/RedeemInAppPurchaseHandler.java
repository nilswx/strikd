package strikd.communication.incoming;

import strikd.sessions.Session;
import strikd.communication.Opcodes;
import strikd.game.items.shop.Shop;
import strikd.game.util.PlatformHelper;
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
		if(PlatformHelper.isApple(session.getPlayer().getPlatform()))
		{
			String receipt = request.readStr();
			shop.redeemAppStoreReceipt(session, receipt);
		}
	}
}
