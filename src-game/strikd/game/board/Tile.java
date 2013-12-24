package strikd.game.board;

import strikd.game.board.triggers.Trigger;

public class Tile extends AbstractTile
{
	private byte tileId;
	private final Board board;

	private Trigger trigger;

	public Tile(byte tileId, int column, char letter, Trigger trigger, Board board)
	{
		super(column, letter);
		
		this.tileId = tileId;
		this.trigger = trigger;
		this.board = board;
	}

	public Board getBoard()
	{
		return this.board;
	}

	public byte getTileId()
	{
		return this.tileId;
	}

	public boolean hasTrigger()
	{
		return (this.trigger != null);
	}

	public void setTrigger(Trigger trigger)
	{
		this.trigger = trigger;
	}

	public Trigger getTrigger()
	{
		return this.trigger;
	}

	@Override
	public int getRow()
	{
		return this.board.getColumn(this.getColumn()).indexOf(this);
	}
	
	@Override
	public String toString()
	{
		if(this.hasTrigger())
		{
			return String.format("[%d,%d] = '%s' + %s", this.getColumn(), this.getRow(), Character.toString(this.getLetter()), this.getTrigger().getTypeName());
		}
		else
		{
			return String.format("[%d,%d] = '%s'", super.getColumn(), this.getRow(), Character.toString(this.getLetter()));
		}
	}
}
