package strikd.game.board.tiles;

import strikd.game.board.triggers.Trigger;

public class Tile
{
	private final int x;
	private final int y;
	private final char letter;
	private final Trigger trigger;
	
	public Tile(int x, int y, char letter)
	{
		this(x, y, letter, null);
	}
	
	public Tile(int x, int y, char letter, Trigger trigger)
	{
		this.x = x;
		this.y = y;
		this.letter = letter;
		this.trigger = trigger;
	}
	
	public final int getX()
	{
		return this.x;
	}
	
	public final int getY()
	{
		return this.y;
	}
	
	public final char getLetter()
	{
		return this.letter;
	}
	
	public final Trigger getTrigger()
	{
		return this.trigger;
	}
}
