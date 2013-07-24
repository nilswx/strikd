package strikd.game.util;

import java.util.Random;

public enum Direction8
{
	North,
	NorthEast,
	East,
	SouthEast,
	South,
	SouthWest,
	West,
	NorthWest;
	
	private static final Direction8[] values = values();
	
	public final Direction8 invert()
	{
		return values[(this.ordinal() + (values.length / 2)) % values.length];
	}
	
	public final Direction8 transform(Direction8 dir)
	{
		return values[(this.ordinal() + dir.ordinal()) % values.length];
	}
	
	public final Direction8 random()
	{
		Random rand = new Random();
		return values[rand.nextInt(values.length)];
	}
}
