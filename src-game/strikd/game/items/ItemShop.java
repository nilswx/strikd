package strikd.game.items;

import org.apache.log4j.Logger;

public class ItemShop
{
	private static final Logger logger = Logger.getLogger(ItemShop.class);
	
	public void reload()
	{
		logger.info("reloaded available items");
	}
}
