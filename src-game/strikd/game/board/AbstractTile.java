package strikd.game.board;

public abstract class AbstractTile
{
	private final int column;
	private final char letter;
	
	public AbstractTile(int column, char letter)
	{
		this.column = column;
		this.letter = letter;
	}
	
	public char getLetter()
	{
		return this.letter;
	}
	
	public int getColumn()
	{
		return this.column;
	}
	
	public abstract int getRow();
}
