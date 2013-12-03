package strikd.game.player;

import strikd.game.items.ItemInventory;
import strikd.game.items.ItemType;
import strikd.util.RandomUtil;
import static strikd.game.items.ItemTypeRegistry._I;

public class PlayerDefaults
{
	public String generateName()
	{
		return String.format("Player-%d", RandomUtil.pickInt(100000, 999999));
	}
	
	public String getMotto()
	{
		return "Hey I'm new!";
	}
	
	public int getBalance()
	{
		return 5;
	}
	
	
	public void stockInventory(Player player)
	{
		// How about some freebies!
		ItemInventory inv = player.getInventory();
		inv.add(_I("HAMMER"), 5);
		inv.add(_I("SWAP"), 3);
		inv.add(_I(RandomUtil.flipCoin() ? "FREEZE" : "WATER"), 2);
		inv.add(_I(RandomUtil.flipCoin() ? "FREEZE" : "WATER"), 2);
		player.saveInventory();
	}
	
	public void giveDefaultAvatar(Player player)
	{
		// Default parts for everyone
		final ItemType[] stockParts =
		{
			// Generic hair
			_I("HR_BLOND_M"),
			_I("HR_BLACK_M"),
			_I("HR_BROWN_M"),
			_I("HR_RED_M"),
			_I("HR_BLOND_F"),
			_I("HR_BLACK_F"),
			_I("HR_BROWN_F"),
			_I("HR_RED_F"),
				
			// Race-friendly heads!
			_I("HD_WHITE_M"),
			_I("HD_WHITE_F"),
			_I("HD_BLACK_M"),
			_I("HD_BLACK_F"),
			
			// Generic eyes
			_I("EY_BLUE"),
			_I("EY_BROWN"),
			_I("EY_GREEN"),
			
			// Generic mouths
			_I("MO_M"),
			_I("MO_F"),
			
			// Generic bodies
			_I("BD_M"),
			_I("BD_F")
		};
		
		// Add them to the inventory
		for(ItemType part : stockParts)
		{
			player.getInventory().add(part);
		}
		player.saveInventory();
		
		// Now generate a random avatar from the stock parts
		player.setAvatar("");
		/*Avatar avatar = player.getAvatar();
		for(AvatarPart part : parts)
		{
			avatar.set(part);
		}*/
	}
}
