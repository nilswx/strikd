package strikd.game.stream.activity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import strikd.game.items.ItemTypeRegistry;
import strikd.game.items.ItemType;
import strikd.game.stream.IntParameterStreamItem;

@Entity @DiscriminatorValue(ItemReceivedStreamItem.TYPE)
public class ItemReceivedStreamItem extends IntParameterStreamItem
{
	public ItemType getItem()
	{
		return ItemTypeRegistry.getType(super.getParameter());
	}
	
	public void setItem(ItemType item)
	{
		super.setParameter(item.getId());
	}
	
	public static final String TYPE = "i";
}
