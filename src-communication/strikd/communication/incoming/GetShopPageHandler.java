package strikd.communication.incoming;

import strikd.communication.Opcodes;
import strikd.communication.outgoing.ShopPageMessage;
import strikd.game.items.shop.Shop;
import strikd.game.items.shop.ShopPage;
import strikd.net.codec.IncomingMessage;
import strikd.sessions.Session;

public class GetShopPageHandler extends MessageHandler
{
	@Override
	public Opcodes.Incoming getOpcode()
	{
		return Opcodes.Incoming.GET_SHOP_PAGE;
	}
	
	@Override
	public void handle(Session session, IncomingMessage request)
	{
		Shop shop = session.getServer().getShop();
		
		ShopPage page = shop.getPage(request.readStr());
		if(page != null)
		{
			session.send(new ShopPageMessage(page));
		}
	}
}
