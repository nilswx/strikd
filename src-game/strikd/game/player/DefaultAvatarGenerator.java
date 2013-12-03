package strikd.game.player;

import static strikd.game.items.ItemTypeRegistry._I;
import strikd.game.items.AvatarPart;
import strikd.game.items.ItemInventory;
import strikd.game.items.ItemType;
import strikd.util.RandomUtil;

public class DefaultAvatarGenerator
{
	public void generateDefaultAvatar(Player player)
	{	
		ItemInventory inv = player.getInventory();
		
		// Add stock bodies
		for(ItemType part : bodies[MALE]) inv.add(part);
		for(ItemType part : bodies[FEMALE]) inv.add(part);
		
		// Add stock heads
		for(ItemType part : heads[MALE]) inv.add(part);
		for(ItemType part : heads[FEMALE]) inv.add(part);

		// Add stock mouths
		for(ItemType part : mouths[MALE]) inv.add(part);
		for(ItemType part : mouths[FEMALE]) inv.add(part);
		
		// Add stock eyes
		for(ItemType part : eyes) inv.add(part);
		
		// Add stock hair
		for(ItemType part : hair[MALE]) inv.add(part);
		for(ItemType part : hair[FEMALE]) inv.add(part);
		
		// Save inventory
		player.saveInventory();
		
		// Pick a random gender
		int gender = RandomUtil.flipCoin() ? 1 : 0;
		
		// Now generate a random avatar from the stock parts
		Avatar ava = new Avatar();
		ava.set((AvatarPart)RandomUtil.pickOne(bodies[gender]));
		ava.set((AvatarPart)RandomUtil.pickOne(heads[gender]));
		ava.set((AvatarPart)RandomUtil.pickOne(mouths[gender]));
		ava.set((AvatarPart)RandomUtil.pickOne(eyes));
		ava.set((AvatarPart)RandomUtil.pickOne(hair[gender]));
		player.setAvatar(ava.toString());
	}
	
	private static final int MALE = 0, FEMALE = 1;
	
	private final ItemType[][] bodies =
	{
		{ _I("BD_M") },
		{ _I("BD_F") },
	};
	
	private final ItemType[][] heads =
	{
		{ _I("HD_WHITE_M"), _I("HD_BLACK_M") },
		{ _I("HD_WHITE_F"), _I("HD_BLACK_F") },
	};
	
	private final ItemType[][] mouths =
	{
		{ _I("MO_M") },
		{ _I("MO_F") },
	};
	
	private final ItemType[] eyes =
	{
		_I("EY_BLUE"), _I("EY_BROWN"), _I("EY_GREEN"),
	};
	
	private final ItemType[][] hair =
	{
		{ _I("HR_BLOND_M"), _I("HR_BLACK_M"), _I("HR_BROWN_M"), _I("HR_RED_M") },
		{ _I("HR_BLOND_F"), _I("HR_BLACK_F"), _I("HR_BROWN_F"), _I("HR_RED_F") },
	};
}
