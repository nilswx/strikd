package strikd.game.items.shop;

import strikd.game.items.ItemType;

public class ShopProduct
{
	private final ItemType item;
	private final int quantity;
	
	public ShopProduct(ItemType item, int quantity)
	{
		this.item = item;
		this.quantity = quantity;
	}
	
	public ItemType getItem()
	{
		return this.item;
	}

	public int getQuantity()
	{
		return this.quantity;
	}
}
