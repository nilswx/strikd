package strikd.game.board;

import java.util.Random;

public enum Direction8
{
	North(0, +1),
	NorthEast(+1, +1),
	East(+1, 0),
	SouthEast(+1, -1),
	South(0, -1),
	SouthWest(-1, -1),
	West(-1, 0),
	NorthWest(-1, +1);
	
	public final int x;
	public final int y;
	
	private Direction8(int deltaX, int deltaY)
	{
		this.x = deltaX;
		this.y = deltaY;
	}
	
	private static final Direction8[] values = values();
	
	public static Direction8[] all()
	{
		return values;
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

    public static final Direction8 directionFromDelta(int deltaX, int deltaY)
    {
        Direction8[] directions = Direction8.all();
        for(int i = 0; i < directions.length; i++)
        {
            Direction8 direction = directions[i];
            if(direction.x == deltaX && direction.y == deltaY)
            {
                return direction;
            }
        }
        return null;
    }
}
