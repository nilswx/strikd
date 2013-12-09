package strikd.game.items;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import strikd.game.items.powerup.*;
import strikd.game.items.shop.Coin;
import strikd.game.items.avatar.*;
import strikd.game.player.Avatar;
import static strikd.game.items.AvatarPart.PartSlot.*;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class ItemTypeRegistry
{
	private static final Logger logger = LoggerFactory.getLogger(ItemTypeRegistry.class);
	
	private static final ItemType[] items = new ItemType[1024];
	private static final List<ItemType> itemList = Lists.newArrayList();
	private static final Map<String, ItemType> itemMap = Maps.newHashMap();
	
	private ItemTypeRegistry() { }
	
	public static ItemType getType(int itemId)
	{
		if(itemId >= 0 && itemId < items.length)
		{
			return items[itemId];
		}
		else
		{
			return null;
		}
	}
	
	public static ItemType _I(int itemId)
	{
		return getType(itemId);
	}

	public static ItemType _I(String code)
	{
		return getType(code);
	}
	
	public static ItemType getType(String code)
	{
		return itemMap.get(code);
	}
	
	public static List<ItemType> allTypes()
	{
		return ImmutableList.copyOf(itemList);
	}
	
	private static void add(ItemType type)
	{
		ItemType other = items[type.getId()];
		if(other != null)
		{
			logger.warn("#{} \"{}\" cannot use the same ID as #{} \"{}\"!",
					type.getId(), type.getCode(),
					other.getId(), other.getCode());
		}
		else if((other = itemMap.get(type.getCode())) != null)
		{
			logger.warn("#{} \"{}\" cannot use the same code as #{} \"{}\"!",
					type.getId(), type.getCode(),
					other.getId(), other.getCode());
		}
		else
		{
			items[type.getId()] = type;
			itemList.add(type);
			itemMap.put(type.getCode(), type);
			
			logger.debug("added #{} \"{}\" [{}]", type.getId(), type.getCode(), type.getClass().getSimpleName());
		}
	}
	
	static
	{
		// TODO: fill register from database that is shared with all server instances
		
		// Internal usage
		int O = 50;
		add(new Coin(O++));
		
		// For coin packs
		int IO = 100;
		add(new GenericAvatarPart(IO++, "BIG_MOUSE_EARS", HAT));
		add(new GenericAvatarPart(IO++, "EYEPATCH", EYES));
		add(new ExperienceBoostingAvatarPart(IO++, "BDAY_HAT", HAT));
		add(new GenericAvatarPart(IO++, "XMAS_HAT", HAT));
		add(new Trophy(IO++, "LIKED"));
		add(new Trophy(IO++, "WINS_5"));
		add(new Trophy(IO++, "XMAS2013"));
		add(new HammerPowerUp(IO++, "HAMMER"));
		add(new FreezePowerUp(IO++, "FREEZE"));
		add(new SwapPowerUp(IO++, "SWAP"));
		add(new GenericAvatarPart(IO++, "ALIEN_EYES", EYES));
		add(new GenericAvatarPart(IO++, "BIG_MOUSTACHE", MOUTH));
		
		// Bodies
		int AO = 500;
		add(new GenericAvatarPart(AO++, "BD_M", BASE));
		add(new GenericAvatarPart(AO++, "BD_F", BASE));
		
		// Heads
		add(new GenericAvatarPart(AO++, "HD_WHITE_M", HEAD));
		add(new GenericAvatarPart(AO++, "HD_WHITE_F", HEAD));
		add(new GenericAvatarPart(AO++, "HD_BLACK_M", HEAD));
		add(new GenericAvatarPart(AO++, "HD_BLACK_F", HEAD));
		
		// Mouths
		add(new GenericAvatarPart(AO++, "MO_M", MOUTH));
		add(new GenericAvatarPart(AO++, "MO_F", MOUTH));
		
		// Eyes
		add(new GenericAvatarPart(AO++, "EY_BLUE", EYES));
		add(new GenericAvatarPart(AO++, "EY_BROWN", EYES));
		add(new GenericAvatarPart(AO++, "EY_GREEN", EYES));
		
		// Hair
		add(new GenericAvatarPart(AO++, "HR_BLOND_M", HAIR));
		add(new GenericAvatarPart(AO++, "HR_BLACK_M", HAIR));
		add(new GenericAvatarPart(AO++, "HR_BROWN_M", HAIR));
		add(new GenericAvatarPart(AO++, "HR_RED_M", HAIR));
		add(new GenericAvatarPart(AO++, "HR_BLOND_F", HAIR));
		add(new GenericAvatarPart(AO++, "HR_BLACK_F", HAIR));
		add(new GenericAvatarPart(AO++, "HR_BROWN_F", HAIR));
		add(new GenericAvatarPart(AO++, "HR_RED_F", HAIR));
	}
	
	public static void main(String[] args)
	{
		// make an avatar
		Avatar av = new Avatar();
		av.set((AvatarPart)_I("ALIEN_EYES"));
		av.set((AvatarPart)_I("BIG_MOUSTACHE"));
		av.set((AvatarPart)_I("XMAS_HAT"));
		
		// parse an avatar
		Avatar av2 = Avatar.parseAvatar(av.toString());
		av2.toString();
		
		// make an inventory
		ItemInventory inv = new ItemInventory();
		inv.add(_I("FREEZE"), 12);
		inv.add(_I("HAMMER"), 4);
		inv.add(_I("SWAP"), 1);
		
		// parse an inventory
		ItemInventory inv2 = ItemInventory.parseInventory(inv.toString());
		inv2.toString();
	}
}
