package strikd.game.board;

import strikd.words.WordDictionary;

import java.util.ArrayList;
import java.util.List;


/**
 * A rectangular space of squares. A square can hold a {@link Square} or <code>null</code>.
 * 
 * @author nilsw
 * 
 */
public abstract class Board
{
    protected int height;
    protected int width;

    protected final ArrayList<Square>[] squares;
	protected final WordDictionary dictionary;
	
	protected Board(int width, int height, WordDictionary dictionary)
	{
        this.width = width;
        this.height = height;

        this.squares = new ArrayList[width];
        for(int x = 0; x < this.getWidth(); x++)
        {
            this.squares[x] = new ArrayList<>();
        }

		this.dictionary = dictionary;
	}

    public ArrayList<Square> getColumn(int index)
    {
        return this.squares[index];
    }
	
	public Square newSquare(int x, int y)
	{
		return new Square(x, y, this);
	}
	
	public final void clear()
	{
		for(int x = 0; x < this.getWidth(); x++)
		{
            ArrayList<Square> column = squares[x];
            column.clear();
		}
	}

    public final void clearSquares(List<Square> squares)
    {
        for(Square square : squares)
        {
            ArrayList<Square> column = this.squares[square.getColumn()];
            column.remove(square.getRow());
        }
    }

	public void rebuild()
	{
		this.clear();
		this.update();
	}
	
	public abstract List<Square> update();
	
	public void destroy()
	{
		// Override me
	}
	
	public final Square getSquare(int x, int y)
	{
		try
        {
            ArrayList<Square> column = this.squares[x];
            return column.get(y);
		}
        catch (IndexOutOfBoundsException e)
        {
            return null;
        }
	}
	
	public final int getWidth()
	{
		return this.width;
	}

	public final int getHeight()
	{
		return this.height;
	}

    public WordDictionary getDictionary()
    {
        return this.dictionary;
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

				Square square = this.getSquare(x, y);
				if(square == null)
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
