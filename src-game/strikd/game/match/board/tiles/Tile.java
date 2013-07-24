package strikd.game.match.board.tiles;

public abstract class Tile
{
	private final int x;
	private final int y;
	
	protected Tile(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public int getX()
	{
		return this.x;
	}
	
	public int getY()
	{
		return this.y;
	}
}
