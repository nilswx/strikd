package strikd.game.board;

import java.io.IOException;

import strikd.game.board.tiles.Tile;

public class GappieBoard extends AbstractBoard
{
	public GappieBoard(int width, int height)
	{
		super(width, height);
	}

	@Override
	public void regenerate()
	{
		super.clear();
		this.fillGaps();
	}
	
	public void fillGaps()
	{
		Tile gap;
		while((gap = this.findGap()) != null)
		{
			this.fillGap(gap);
		}
	}
	
	private void fillGap(Tile gap)
	{
		
	}
	
	private Tile findGap()
	{
		for(int x = 0; x < this.getWidth(); x++)
		{
			for(int y = 0; y < this.getHeight(); y++)
			{
				if(this.squares[x][y] == null)
				{
					return this.squares[x][y];
				}
			}
		}
		
		return null;
	}
	
	public static void main(String[] args) throws IOException
	{
		AbstractBoard abstractBoard = new GappieBoard(5, 5);
		abstractBoard.regenerate();
		
		System.out.println(abstractBoard.toString());
		System.out.println();
		System.out.println(abstractBoard.toLongString());
	}
}
