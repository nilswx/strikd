package strikd.game.board;

import strikd.game.board.tiles.Square;
import strikd.game.board.tiles.Tile;

/**
 * A rectangular space of squares. A square can hold a {@link Tile} or <code>null</code>.
 * 
 * @author nilsw
 * 
 */
public abstract class Board
{
	protected final Square[][] squares;
	
	private final BoardUpdateGenerator updates;
	
	protected Board(int width, int height)
	{
		this.squares = new Square[width][height];
		this.updates = new BoardUpdateGenerator(this);
	}

	public final void clear()
	{
		for(int x = 0; x < this.getWidth(); x++)
		{
			for(int y = 0; y < this.getHeight(); y++)
			{
				if(this.squares[x][y] == null)
				{
					this.squares[x][y] = new Square(x, y, this);
				}
				this.squares[x][y].clear();
			}
		}
	}

	public abstract void regenerate();

	public final Square getSquare(int x, int y)
	{
		return this.squares[x][y];
	}
	
	public final boolean squareExists(int x, int y)
	{
		return (x >= 0 && x < this.getWidth() && y >= 0 && y < this.getWidth());
	}
	
	public final int getWidth()
	{
		return this.squares.length;
	}

	public final int getHeight()
	{
		return this.squares[0].length;
	}
	
	public BoardUpdateGenerator getUpdateGenerator()
	{
		return this.updates;
	}
	
	@Override
	public final String toString()
	{
		return String.format("%s %dx%d", this.getClass().getSimpleName(), this.getWidth(), this.getHeight());
	}
	
	public final String toMatrixString()
	{
		StringBuilder sb = new StringBuilder();
		
		for(int y = this.getHeight() - 1; y >= 0; y--)
		{
			sb.append(y);
			sb.append("  ");
			for(int x = 0; x < this.getWidth(); x++)
			{
				sb.append('[');
				
				Square square = this.squares[x][y];
				if(square.isNull())
				{
					sb.append("   ");
				}
				else
				{
					if(!square.hasTrigger())
					{
						sb.append(' ');
						sb.append(square.getLetter());
						sb.append(' ');
					}
					else
					{
						sb.append(square.getLetter());
						sb.append(':');
						sb.append(Character.toUpperCase(square.getTrigger().getTypeName().charAt(0)));
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
