package strikd.game.board;

public class IndexedBoard extends Board
{
	protected IndexedBoard(int width, int height)
	{
		super(width, height);
	}

	@Override
	public void regenerate()
	{
		this.clear();
	}
}
