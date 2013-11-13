package strikd.game.board;

public class FutureTile extends Square
{
	public final char letter;

	public FutureTile(int column, int row, char letter)
	{
		super(column, row);
		this.letter = letter;
	}
	
	public String toString()
	{
		return Character.toString(letter);
	}
}
