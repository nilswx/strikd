package strikd.game.player;

import java.util.HashMap;
import java.util.Map.Entry;

import strikd.game.player.Avatar.PartType;

@SuppressWarnings("serial")
public class Avatar extends HashMap<PartType, String>
{
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		
		for(Entry<PartType, String> part : this.entrySet())
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
