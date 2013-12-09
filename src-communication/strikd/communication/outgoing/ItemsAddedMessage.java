package strikd.communication.outgoing;

import java.util.List;

import strikd.communication.Opcodes;
import strikd.game.items.shop.ShopProduct;
import strikd.net.codec.OutgoingMessage;

public class ItemsAddedMessage extends OutgoingMessage
{
	public ItemsAddedMessage(List<ShopProduct> added)
	{
		super(Opcodes.Outgoing.ITEMS_ADDED);
		super.writeInt(added.size());
		for(ShopProduct product : added)
		{
			super.writeInt(product.getItem().getId());
			super.writeInt(product.getQuantity());
		}
	}
}
