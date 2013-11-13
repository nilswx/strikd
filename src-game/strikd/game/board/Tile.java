package strikd.game.board;

import strikd.game.board.triggers.Trigger;
import strikd.game.match.MatchPlayer;

public class Tile
{
	private byte tileId;
	private final int column;
	private final Board board;

	private char letter;
	private Trigger trigger;

	private MatchPlayer firstSelector;
	private MatchPlayer secondSelector;

	public Tile(byte tileId, int column, char letter, Trigger trigger, Board board)
	{
		this.tileId = tileId;
		this.column = column;
		this.letter = letter;
		this.trigger = trigger;
		this.board = board;
	}

	public void remove()
	{
		if(this.firstSelector != null)
		{
			this.firstSelector.clearSelection();
		}
		
		if(this.secondSelector != null)
		{
			this.secondSelector.clearSelection();
		}
	}
	
	public void onSelect(MatchPlayer player)
	{
		if(this.firstSelector == null)
		{
			this.firstSelector = player;
		}
		else if(this.secondSelector == null)
		{
			this.secondSelector = player;
		}
	}
	
	public void onDeselect(MatchPlayer player)
	{
		if(player == this.firstSelector)
		{
			this.firstSelector = null;
		}
		else if(player == this.secondSelector)
		{
			this.secondSelector = null;
		}
	}

	public Board getBoard()
	{
		return this.board;
	}

	public byte getTileId()
	{
		return this.tileId;
	}

	public char getLetter()
	{
		return this.letter;
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

	public int getColumn()
	{
		return this.column;
	}

	public int getRow()
	{
		return this.board.getColumn(this.column).indexOf(this);
	}

	public boolean isSelected()
	{
		return (this.firstSelector != null || this.secondSelector != null);
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
