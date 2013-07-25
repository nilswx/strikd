package strikd.game.board;

public class IndexedBoard extends AbstractBoard
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
