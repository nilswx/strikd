package strikd.game.player;

import javax.persistence.Embeddable;

@Embeddable
public class Avatar /*extends HashMap<PartType, String>*/
{
	/*
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
	}*/
	
	public enum PartType
	{
		HAT,
		HAIR,
		HEAD,
		EYES,
		MOUTH,
		TORSO
	}
}
