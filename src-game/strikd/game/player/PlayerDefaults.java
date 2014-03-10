package strikd.game.player;

import strikd.game.items.ItemInventory;
import strikd.util.RandomUtil;
import static strikd.game.items.ItemTypeRegistry._I;

public class PlayerDefaults
{
	public String generateName()
	{
		return String.format("Player-%d", RandomUtil.pickInt(100000, 999999));
	}
	
	public String generateAvatar()
	{
		return Integer.toString(RandomUtil.pickInt(1, Avatars.AMOUNT_OF_AVATARS));
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
}
