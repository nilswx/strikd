package strikd.game.player;

import strikd.game.items.AvatarPart;
import strikd.game.items.AvatarPart.PartType;

public final class Avatar
{
	private static final char PART_DELIMITER = ':';
	
	private final AvatarPart[] parts = new AvatarPart[PartType.values().length];
	
	public AvatarPart get(PartType type)
	{
		return this.parts[type.ordinal()];
	}
	
	public boolean has(PartType type)
	{
		return (this.get(type) != null);
	}
	
	public boolean hasPart(AvatarPart part)
	{
		return (this.get(part.getType()) == part);
	}
	
	private void set(PartType type, AvatarPart part)
	{
		this.parts[type.ordinal()] = part;
	}

	public void set(AvatarPart part)
	{
		this.set(part.getType(), part);
	}
	
	public void remove(PartType type)
	{
		this.set(type, null);
	}
	
	public void remove(AvatarPart part)
	{
		if(this.hasPart(part))
		{
			this.remove(part.getType());
		}
	}
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		
		boolean sep = false;
		for(int i = 0; i < this.parts.length; i++)
		{
			if(this.parts[i] != null)
			{
				if(sep) sb.append(PART_DELIMITER); else sep = true;
				sb.append(this.parts[i].getType().code());
				sb.append(this.parts[i].getId());
			}
		}
		
		return sb.toString();
	}
}
