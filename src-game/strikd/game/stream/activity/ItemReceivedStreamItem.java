package strikd.game.stream.activity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import strikd.game.items.ItemType;

@Entity @DiscriminatorValue(ItemReceivedStreamItem.TYPE)
public class ItemReceivedStreamItem extends ActivityStreamItem
{
	@Enumerated(EnumType.STRING)
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
