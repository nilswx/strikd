package strikd.communication.outgoing;

import java.util.Set;

import strikd.communication.Opcodes;
import strikd.net.codec.OutgoingMessage;

public class InAppPurchaseProductsMessage extends OutgoingMessage
{
	public InAppPurchaseProductsMessage(Set<String> productIds)
	{
		super(Opcodes.Outgoing.IN_APP_PURCHASE_PRODUCTS);
		super.writeInt(productIds.size());
		for(String productId : productIds)
		{
			super.writeStr(productId);
		}
	}
}
