package strikd.game.items;

import strikd.communication.outgoing.ItemTypesMessage;

public class ItemTypesMessageCache
{
	private static ItemTypesMessage message;
	
	public static ItemTypesMessage getMessage()
	{
		return message;
	}
	
	public static void rebuildMessage()
	{
		message = new ItemTypesMessage(ItemTypeRegistry.allTypes());
	}
	
	static
	{
		rebuildMessage();
	}
}
