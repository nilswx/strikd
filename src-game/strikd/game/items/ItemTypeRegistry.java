package strikd.game.items;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import strikd.game.items.powerup.*;
import strikd.game.items.avatar.*;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

public class ItemTypeRegistry
{
	private static final Logger logger = LoggerFactory.getLogger(ItemTypeRegistry.class);
	
	private static final ItemType[] items = new ItemType[1024];
	private static final List<ItemType> itemList = Lists.newArrayList();
	
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
		else
		{
			items[type.getId()] = type;
			itemList.add(type);
			
			logger.debug("added #{} \"{}\" [{}]", type.getId(), type.getCode(), type.getClass().getSimpleName());
		}
	}
	
	static
	{
		// TODO: fill register from .json file that can be shared with client too
		add(new GenericAvatarPart(48, "BIG_MOUSE_EARS"));
		add(new GenericAvatarPart(49, "EYEPATCH"));
		add(new ExperienceBoostingAvatarPart(77, "BDAY_HAT"));
		add(new GenericAvatarPart(61, "XMAS_HAT"));
		add(new Trophy(512, "LIKED"));
		add(new Trophy(513, "WINS_5"));
		add(new Trophy(514, "XMAS2013"));
		add(new HammerPowerUp(14, "HAMMER"));
		add(new FreezePowerUp(17, "FREEZE"));
		add(new SwapPowerUp(20, "SWAP"));
	}
}
