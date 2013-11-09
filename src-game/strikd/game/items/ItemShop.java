package strikd.game.items;

import org.apache.log4j.Logger;

import strikd.Server;
import strikd.game.player.Player;

public class ItemShop extends Server.Referent
{
	private static final Logger logger = Logger.getLogger(ItemShop.class);
	
	public ItemShop(Server server)
	{
		super(server);
	}

	public void reload()
	{
		logger.info("reloaded available items");
	}
	
	public Item purchaseItem(int typeId, Player player)
	{
		// TODO: validate item type and player currency balance
		
		// TODO: write transaction log for player (and statistics)
		
		// TODO: create and return item
		
		return null;
	}
}
