package strikd.game.player;

import strikd.game.items.AvatarPart;
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
		final int MALE = 0, FEMALE = 1;
		
		// Add stock bodies
		final ItemType[][] bodies =
		{
			{ _I("BD_M") },
			{ _I("BD_F") },
		};
		for(ItemType part : bodies[MALE]) player.getInventory().add(part);
		for(ItemType part : bodies[FEMALE]) player.getInventory().add(part);
		
		// Add stock heads
		final ItemType[][] heads =
		{
			{ _I("HD_WHITE_M"), _I("HD_BLACK_M") },
			{ _I("HD_WHITE_F"), _I("HD_BLACK_F") },
		};
		for(ItemType part : heads[MALE]) player.getInventory().add(part);
		for(ItemType part : heads[FEMALE]) player.getInventory().add(part);
		
		// Add stock mouths
		final ItemType[][] mouths =
		{
			{ _I("MO_M") },
			{ _I("MO_F") },
		};
		for(ItemType part : mouths[MALE]) player.getInventory().add(part);
		for(ItemType part : mouths[FEMALE]) player.getInventory().add(part);
		
		// Add stock eyes
		final ItemType[] eyes =
		{
			_I("EY_BLUE"), _I("EY_BROWN"), _I("EY_GREEN"),
		};
		for(ItemType part : eyes) player.getInventory().add(part);
		
		// Add stock hair
		final ItemType[][] hair =
		{
			{ _I("HR_BLOND_M"), _I("HR_BLACK_M"), _I("HR_BROWN_M"), _I("HR_RED_M") },
			{ _I("HR_BLOND_F"), _I("HR_BLACK_F"), _I("HR_BROWN_F"), _I("HR_RED_F") },
		};
		for(ItemType part : hair[MALE]) player.getInventory().add(part);
		for(ItemType part : hair[FEMALE]) player.getInventory().add(part);
		
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
}
