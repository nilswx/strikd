package strikd.game.match.board;

public class SimpleBoard extends Board
{
	public SimpleBoard(int width, int height)
	{
		super(width, height);
	}

	@Override
	public void refresh()
	{
		super.clear();
	}
}
