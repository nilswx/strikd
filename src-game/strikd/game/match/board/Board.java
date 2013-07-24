package strikd.game.match.board;

import strikd.game.match.board.tiles.Tile;

/**
 * A rectangular space of squares. A square can hold a {@link Tile} or <code>null</code>.
 * 
 * @author nilsw
 * 
 */
public abstract class Board
{
	private final Tile[][] squares;

	protected Board(int width, int height)
	{
		this.squares = new Tile[width][height];
	}

	public final void clear()
	{
		for(int x = 0; x < this.getWidth(); x++)
		{
			for(int y = 0; y < this.getHeight(); y++)
			{
				this.squares[x][y] = null;
			}
		}
	}

	public abstract void refresh();

	public Tile getTile(int x, int y)
	{
		return this.squareExists(x, y) ? this.squares[x][y] : null;
	}
	
	protected void setTile(Tile tile)
	{
		int x = tile.getX(), y = tile.getY();
		if(this.squareExists(x, y))
		{
			this.squares[x][y] = tile;
		}
	}

	public boolean squareExists(int x, int y)
	{
		return (x >= 0 && x < this.getWidth() && y >= 0 && y < this.getWidth());
	}

	public int getWidth()
	{
		return this.squares.length;
	}

	public int getHeight()
	{
		return this.squares[0].length;
	}
}
