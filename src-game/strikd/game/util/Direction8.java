package strikd.game.util;

import java.awt.Point;
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
	
	public final boolean isDiagonal()
	{
		return (this.ordinal() % 2 != 0);
	}
	
	public static final Direction8 random()
	{
		Random rand = new Random();
		return values[rand.nextInt(values.length)];
	}
	
	public final Point getDiff()
	{
		switch(this)
		{
			case North: return new Point(0, +1);
			case NorthEast: return new Point(+1, +1);
			case East: return new Point(+1, 0);
			case SouthEast: return new Point(+1, -1);
			case South: return new Point(0, -1);
			case SouthWest: return new Point(-1, -1);
			case West: return new Point(-1, 0);
			case NorthWest: return new Point(-1, +1);
		}
		
		return new Point();
	}
}
