package strikd.game.board;

import strikd.game.board.tiles.Tile;

/**
 * A rectangular space of squares. A square can hold a {@link Tile} or <code>null</code>.
 * 
 * @author nilsw
 * 
 */
public abstract class Board
{
	protected final Tile[][] tiles;

	protected Board(int width, int height)
	{
		this.tiles = new Tile[width][height];
	}

	public final void clear()
	{
		for(int x = 0; x < this.getWidth(); x++)
		{
			for(int y = 0; y < this.getHeight(); y++)
			{
				this.tiles[x][y] = null;
			}
		}
	}

	public abstract void regenerate();

	public final Tile getTile(int x, int y)
	{
		return this.squareExists(x, y) ? this.tiles[x][y] : null;
	}
	
	protected final void setTile(Tile tile)
	{
		int x = tile.getX(), y = tile.getY();
		if(this.squareExists(x, y))
		{
			this.tiles[x][y] = tile;
		}
	}

	public final boolean squareExists(int x, int y)
	{
		return (x >= 0 && x < this.getWidth() && y >= 0 && y < this.getWidth());
	}

	public final int getDimension()
	{
		return this.tiles.length;
	}
	
	public final int getWidth()
	{
		return this.tiles.length;
	}

	public final int getHeight()
	{
		return this.tiles[0].length;
	}
	
	@Override
	public final String toString()
	{
		return String.format("%s %dx%d", this.getClass().getSimpleName(), this.getWidth(), this.getHeight());
	}
	
	public final String toLongString()
	{
		StringBuilder sb = new StringBuilder();
		
		for(int y = 0; y < this.getHeight(); y++)
		{
			sb.append(y);
			sb.append("  ");
			for(int x = 0; x < this.getWidth(); x++)
			{
				sb.append('[');
				
				Tile tile = this.tiles[x][y];
				if(tile == null)
				{
					sb.append("   ");
				}
				else
				{
					if(tile.getTrigger() == null)
					{
						sb.append(' ');
						sb.append(tile.getLetter());
						sb.append(' ');
					}
					else
					{
						sb.append(tile.getLetter());
						sb.append(':');
						sb.append(Character.toUpperCase(tile.getTrigger().getTypeName().charAt(0)));
					}
				}
				
				sb.append(']');
			}
			sb.append(System.lineSeparator());
		}
		
		sb.append(System.lineSeparator());
		sb.append("   ");
		for(int x = 0; x < this.getWidth(); x++)
		{
			sb.append("  ");
			sb.append(x);
			sb.append("  ");
		}
		
		return sb.toString();
	}
}
