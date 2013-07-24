package strikd.game.match.board.tiles;

public class Tile
{
	private final int x;
	private final int y;
	private final char letter;
	
	public Tile(int x, int y, char letter)
	{
		this.x = x;
		this.y = y;
		this.letter = letter;
	}
	
	public int getX()
	{
		return this.x;
	}
	
	public int getY()
	{
		return this.y;
	}
	
	public char getLetter()
	{
		return this.letter;
	}
}
