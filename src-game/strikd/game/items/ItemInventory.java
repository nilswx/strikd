package strikd.game.items;

import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Maps;

public class ItemInventory
{
	private static final char ITEM_DELIMITER = '|';
	private static final char QUANTITY_DELIMITER = 'x';
	
	private final Map<ItemType, Integer> items = Maps.newHashMap();
	
	public void add(ItemType type)
	{
		this.add(type, 1);
	}
	
	public void add(ItemType type, int amount)
	{
		if(type != null)
		{
			this.items.put(type, this.getAmount(type) + amount);
		}
	}
	
	public void take(ItemType type)
	{
		this.take(type, 1);
	}
	
	public void take(ItemType type, int amount)
	{
		Integer current = this.items.get(type);
		if(current != null)
		{
			if(current > amount)
			{
				this.items.put(type, (current - amount));
			}
			else
			{
				this.items.remove(type);
			}
		}
	}
	
	public boolean has(ItemType type)
	{
		return this.items.containsKey(type);
	}
	
	public int getAmount(ItemType type)
	{
		Integer amount = this.items.get(type);
		return (amount != null) ? amount : 0;
	}
	
	public void clear()
	{
		this.items.clear();
	}
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		
		boolean first = true;
		for(Entry<ItemType, Integer> item : this.items.entrySet())
		{
			if(!first) sb.append(ITEM_DELIMITER); else first = false;
			sb.append(item.getValue());
			sb.append(QUANTITY_DELIMITER);
			sb.append(item.getKey().getId());
		}
		
		return sb.toString();
	}
	
	public static ItemInventory parseInventory(String str)
	{
		ItemInventory inv = new ItemInventory();
		
		if(str != null)
		{
			for(int pos = 0, end = 0; end != -1; pos = end + 1)
			{
				end = str.indexOf(ITEM_DELIMITER, pos);
				String item = (end != -1 ? str.substring(pos, end) : str.substring(pos));
				
				int split = item.indexOf(QUANTITY_DELIMITER);
				int amount = Integer.parseInt(item.substring(0, split));
				ItemType type = ItemTypeRegistry.getType(Integer.parseInt(item.substring(split + 1)));
				
				if(type != null && amount > 0)
				{
					inv.items.put(type, amount);
				}
			}
		}
		
		return inv;
	}
}