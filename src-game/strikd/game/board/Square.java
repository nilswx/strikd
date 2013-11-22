package strikd.game.board;

public class Square
{
	public final int column;
	public final int row;
	
	public Square(int column, int row)
	{
		this.column = column;
		this.row = row;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + this.column;
		result = prime * result + this.row;
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if(obj instanceof Square)
		{
			Square other = (Square)obj;
			return (this.column == other.column && this.row == other.row);
		}
		else
		{
			return false;
		}
	}
}
