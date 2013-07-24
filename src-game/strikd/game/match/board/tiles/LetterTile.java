package strikd.game.match.board.tiles;

public class LetterTile extends Tile
{
	private final char letter;
	
	public LetterTile(int x, int y, char letter)
	{
		super(x, y);
		this.letter = letter;
	}
	
	public char getLetter()
	{
		return this.letter;
	}
}
