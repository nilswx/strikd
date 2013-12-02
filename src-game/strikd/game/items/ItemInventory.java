package strikd.game.items;

import java.util.EnumMap;
import java.util.Map.Entry;
import java.util.StringTokenizer;

import com.google.common.collect.Maps;

import static strikd.game.items.ItemType.*;

public class ItemInventory
{
	private static final char ITEM_DELIMITER = '|';
	private static final char QUANTITY_DELIMITER = 'x';
	
	private final EnumMap<ItemType, Integer> items = Maps.newEnumMap(ItemType.class);
	
	public void add(ItemType type, int amount)
	{
		this.items.put(type, this.getAmount(type) + amount);
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
			sb.append(item.getKey().ordinal());
		}
		
		return sb.toString();
	}
	
	public static ItemInventory parseInventory(String str)
	{
		ItemInventory inv = new ItemInventory();
		
		StringTokenizer st = new StringTokenizer(str, Character.toString(ITEM_DELIMITER));
		while(st.hasMoreTokens())
		{
			String item = st.nextToken();
			
			int split = item.indexOf(QUANTITY_DELIMITER);
			int quantity = Integer.parseInt(item.substring(0, split));
			ItemType type = ItemType.valueOf(Integer.parseInt(item.substring(split + 1)));
			
			if(type != null)
			{
				inv.add(type, quantity);
			}
		}
		
		return inv;
	}
	
	public static void main(String[] args)
	{
		ItemInventory inv = new ItemInventory();
		inv.add(SAND, 5);
		inv.add(EARTHQUAKE, 1);
		inv.add(HAMMER, 8);
		
		String str = inv.toString();
		System.out.println(str);
		
		ItemInventory inv2 = ItemInventory.parseInventory(str);
		System.out.println(inv2.toString());
		
		inv2.take(HAMMER, 5);
		System.out.println(inv2);
		
		inv2.take(HAMMER, 6);
		System.out.println(inv2);
	}
}
