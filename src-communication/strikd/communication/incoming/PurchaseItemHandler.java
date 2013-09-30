package strikd.communication.incoming;

import strikd.sessions.Session;
import strikd.communication.Opcodes;
import strikd.communication.outgoing.AlertMessage;
import strikd.communication.outgoing.ItemAddedMessage;
import strikd.game.items.Item;
import strikd.game.items.ItemShop;
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
		// Determine what item type to purchase
		int typeId = request.readInt();
		
		// Attempt to purchase the item
		ItemShop shop = session.getServer().getShop();
		Item item = shop.purchaseItem(typeId, session.getUser());
		
		// Purchased successfully?
		if(item == null)
		{
			session.send(new AlertMessage("Purchase failed! You have not been charged."));
		}
		else
		{
			session.getUser().items.add(item);
			session.send(new ItemAddedMessage(item));
		}
	}
}
