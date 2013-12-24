package strikd.game.board;

public class VirtualTile extends AbstractTile
{
	private final int row;
	
	public VirtualTile(int column, int row, char letter)
	{
		super(column, letter);
		this.row = row;
	}

	@Override
	public int getRow()
	{
		return this.row;
	}
	
	@Override
	public String toString()
	{
		return Character.toString(super.getLetter());
	}
}
