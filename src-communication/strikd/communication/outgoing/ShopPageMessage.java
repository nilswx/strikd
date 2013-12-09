package strikd.communication.outgoing;

import strikd.communication.Opcodes;
import strikd.game.items.shop.ShopOffer;
import strikd.game.items.shop.ShopPage;
import strikd.game.items.shop.ShopProduct;
import strikd.net.codec.OutgoingMessage;

public class ShopPageMessage extends OutgoingMessage
{
	public ShopPageMessage(ShopPage page)
	{
		super(Opcodes.Outgoing.SHOP_PAGE);
		super.writeStr(page.getId());
		
		super.writeByte(page.getOffers().size());
		for(ShopOffer offer : page.getOffers())
		{
			super.writeInt(offer.getId());
			super.writeInt(offer.getPrice());
			
			super.writeByte(offer.getProducts().size());
			for(ShopProduct product : offer.getProducts())
			{
				super.writeInt(product.getItem().getId());
				super.writeByte(product.getQuantity());
			}
		}
	}
}
