package strikd.game.player;

import strikd.game.items.AvatarPart;
import strikd.game.items.AvatarPart.PartType;

public class Avatar
{
	private final AvatarPart[] parts = new AvatarPart[PartType.values().length];
	
	public AvatarPart getPart(PartType type)
	{
		return this.parts[type.ordinal()];
	}
	
	public void setPart(AvatarPart part)
	{
		this.parts[part.getType().ordinal()] = part;
	}
	
	public boolean hasPart(PartType type)
	{
		return (this.getPart(type) != null);
	}
	
	public boolean hasPart(AvatarPart part)
	{
		return (this.getPart(part.getType()) == part);
	}
}
