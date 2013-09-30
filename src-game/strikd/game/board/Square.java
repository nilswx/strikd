package strikd.game.board;

import strikd.game.board.triggers.Trigger;

public class Square
{
	public static int count;
	
	public final int x;
	public final int y;
	private final Board board;
	
	private char letter;
	private Trigger trigger;
	
	private boolean needsUpdate;
	
	public Square(int x, int y, Board board)
	{
		this.x = x;
		this.y = y;
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
			this.board.getUpdateGenerator().registerUpdate();
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
	
	public boolean isNull()
	{
		return (this.letter == 0);
	}
	
	public boolean isTile()
	{
		return !this.isNull();
	}
	
	public char getLetter()
	{
		if(this.isNull())
		{
			throw new IllegalStateException("has no letter");
		}
	
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
	
	@Override
	public String toString()
	{
		if(this.isNull())
		{
			return String.format("[%d,%d] = NULL", this.x, this.y);
		}
		else
		{
			if(this.hasTrigger())
			{
				return String.format("[%d,%d] = '%s' + %s", this.x, this.y, Character.toString(this.letter), this.trigger.getTypeName());
			}
			else
			{
				return String.format("[%d,%d] = '%s'", this.x, this.y, Character.toString(this.letter));
			}
		}
	}
}
