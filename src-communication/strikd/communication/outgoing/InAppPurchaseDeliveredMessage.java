package strikd.communication.outgoing;

import strikd.communication.Opcodes;
import strikd.net.codec.OutgoingMessage;

public class InAppPurchaseDeliveredMessage extends OutgoingMessage
{
	public InAppPurchaseDeliveredMessage(long transactionId)
	{
		super(Opcodes.Outgoing.IN_APP_PURCHASE_DELIVERED);
		super.writeLong(transactionId);
	}
}
