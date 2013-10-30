package strikd.game.player;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Avatar
{
	private Map<PartType, String> parts = new HashMap<PartType, String>();
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		
		for(Entry<PartType, String> part : this.parts.entrySet())
		{
			sb.append(part.getKey());
			sb.append(':');
			sb.append(part.getValue());
		}
		
		return sb.toString();
	}
	
	public enum PartType
	{
		Hat, // birthday hat!
		Hair,
		Head,
		Eyes,
		Mouth,
		Shirt
	}
}
