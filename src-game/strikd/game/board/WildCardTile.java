package strikd.game.board;

public class WildCardTile
{
	public final int row;
	public final int column;
	private char letter;
	
    public WildCardTile(int row, int column)
    {
    	this.row = row;
    	this.column = column;
    }

    public void setLetter(char letter)
    {
        this.letter = letter;
    }
}
