package strikd.game.items;

import org.apache.log4j.Logger;

import strikd.Server;

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
}
