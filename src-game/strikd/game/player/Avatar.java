package strikd.game.player;

import strikd.game.items.AvatarPart;
import strikd.game.items.AvatarPart.PartType;

public final class Avatar
{
	private final AvatarPart[] parts = new AvatarPart[PartType.values().length];
	
	public final AvatarPart get(PartType type)
	{
		return this.parts[type.ordinal()];
	}
	
	public final boolean has(PartType type)
	{
		return (this.get(type) != null);
	}
	
	public final boolean hasPart(AvatarPart part)
	{
		return (this.get(part.getType()) == part);
	}
	
	private final void set(PartType type, AvatarPart part)
	{
		this.parts[type.ordinal()] = part;
	}
	
	public final void set(AvatarPart part)
	{
		this.set(part.getType(), part);
	}
	
	public final void remove(PartType type)
	{
		this.set(type, null);
	}
	
	public final void remove(AvatarPart part)
	{
		if(this.hasPart(part))
		{
			this.remove(part.getType());
		}
	}
}
