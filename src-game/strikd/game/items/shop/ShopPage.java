package strikd.game.items.shop;

import java.util.List;

import com.google.common.collect.Lists;

public class ShopPage
{
	private final String id;
	private final List<ShopOffer> offers;
	
	public ShopPage(String id)
	{
		this.id = id;
		this.offers = Lists.newArrayList();
	}
	
	public String getId()
	{
		return this.id;
	}
	
	public List<ShopOffer> getOffers()
	{
		return this.offers;
	}
	
	@Override
	public String toString()
	{
		return String.format("\"%s\" offers=%d", this.id, this.offers.size());
	}
}
