package strikd.game.items.shop;

import java.util.List;

import com.google.common.collect.Lists;

public class ShopOffer
{
	private final int id;
	private final int price;
	private final List<ShopProduct> products;
	
	public ShopOffer(int id, int price)
	{
		this.id = id;
		this.price = price;
		this.products = Lists.newArrayList();
	}
	
	public int getId()
	{
		return this.id;
	}
	
	public int getPrice()
	{
		return this.price;
	}
	
	public List<ShopProduct> getProducts()
	{
		return this.products;
	}
}
