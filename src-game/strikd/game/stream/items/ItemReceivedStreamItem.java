package strikd.game.stream.items;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import strikd.game.items.ItemType;
import strikd.game.stream.ActivityStreamItem;

@Entity @DiscriminatorValue(ItemReceivedStreamItem.TYPE)
public class ItemReceivedStreamItem extends ActivityStreamItem
{
	private ItemType item;

	public ItemType getItem()
	{
		return this.item;
	}

	public void setItem(ItemType item)
	{
		this.item = item;
	}
	
	public static final String TYPE = "i";
}
