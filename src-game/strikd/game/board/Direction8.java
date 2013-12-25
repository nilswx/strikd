package strikd.game.board;

import java.util.Random;

public enum Direction8
{
	NORTH(0, +1),
	NORTH_EAST(+1, +1),
	EAST(+1, 0),
	SOUTH_EAST(+1, -1),
	SOUTH(0, -1),
	SOUTH_WEST(-1, -1),
	WEST(-1, 0),
	NORTH_WEST(-1, +1);

	public final int x;
	public final int y;

	private Direction8(int deltaX, int deltaY)
	{
		this.x = deltaX;
		this.y = deltaY;
	}

	private static final Direction8[] values = values();
	private static final Direction8[] nesw = { NORTH, EAST, SOUTH, WEST };
	
	public static Direction8[] all()
	{
		return values;
	}
	
	public static Direction8[] noDiagonals()
	{
		return nesw;
	}
	
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

	private static final Direction8[][] INVERTED_LOOKUP = new Direction8[3][3];

	static
	{
		for(Direction8 dir : all())
		{
			INVERTED_LOOKUP[dir.x + 1][dir.y + 1] = dir;
		}
	}

	public static final Direction8 directionFromDelta(int deltaX, int deltaY)
	{
		return INVERTED_LOOKUP[deltaX + 1][deltaY + 1];
	}
}
