package strikd.game.items;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import strikd.game.items.powerup.*;
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
		add(new GenericAvatarPart(48, "BIG_MOUSE_EARS", HAT));
		add(new GenericAvatarPart(49, "EYEPATCH", EYES));
		add(new ExperienceBoostingAvatarPart(77, "BDAY_HAT", HAT));
		add(new GenericAvatarPart(61, "XMAS_HAT", HAT));
		add(new Trophy(512, "LIKED"));
		add(new Trophy(513, "WINS_5"));
		add(new Trophy(514, "XMAS2013"));
		add(new HammerPowerUp(14, "HAMMER"));
		add(new FreezePowerUp(17, "FREEZE"));
		add(new SwapPowerUp(20, "SWAP"));
		add(new GenericAvatarPart(52, "ALIEN_EYES", EYES));
		add(new GenericAvatarPart(53, "BIG_MOUSTACHE", MOUTH));
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
