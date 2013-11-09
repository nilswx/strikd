package strikd.game.board;

import strikd.game.board.triggers.Trigger;

import java.util.List;

public class Square
{
	private final int x;
    protected int y = Integer.MIN_VALUE;

	private final Board board;
	
	protected char letter;
	private Trigger trigger;
	
	private boolean needsUpdate;

    public final static char WILDCARD_CHARACTER = '?';

	public Square(int x, int y, Board board)
	{
		this.x = x;
		this.board = board;
	}
	
	public void clear()
	{
		this.letter = 0;
		this.trigger = null;
		this.needUpdate();
	}
	
	public boolean needsUpdate()
	{
		return this.needsUpdate;
	}
	
	private void needUpdate()
	{
		if(!this.needsUpdate)
		{
			this.needsUpdate = true;
			//this.board.getUpdateGenerator().registerUpdate();
		}
	}
	
	public void updated()
	{
		this.needsUpdate = false;
	}
	
	public Board getBoard()
	{
		return this.board;
	}
	
	public char getLetter()
	{
		return this.letter;
	}
	
	public void setLetter(char letter)
	{
		this.letter = letter;
		this.needUpdate();
	}
	
	public boolean hasTrigger()
	{
		return (this.trigger != null);
	}
	
	public void setTrigger(Trigger trigger)
	{
		this.trigger = trigger;
		this.needUpdate();
	}
	
	public Trigger getTrigger()
	{
		return this.trigger;
	}

    public boolean isTile()
    {
        return (this == this.board.getSquare(this.getColumn(), this.getRow()));
    }

    public int getColumn()
    {
        return this.x;
    }

    public int getRow()
    {
        List<Square> column = this.board.getColumn(x);
        return column.indexOf(this);
    }

    public void freeze()
    {
        this.y = this.getRow();
    }

    public int getFrozenRow()
    {
        return this.y;
    }

	@Override
	public String toString()
	{
        if(this.hasTrigger())
        {
            return String.format("[%d,%d] = '%s' + %s", this.getColumn(), this.getRow(), Character.toString(this.letter), this.trigger.getTypeName());
        }
        else
        {
            return String.format("[%d,%d] = '%s'", this.getColumn(), this.getRow(), Character.toString(this.letter));
        }
	}
}
