package strikd.game.board;

import strikd.game.board.triggers.Trigger;
import strikd.game.match.MatchPlayer;

import java.util.List;

public class Tile
{
	private byte tileId;
	private final int column;

	private final Board board;

	protected char letter;
	private Trigger trigger;

	private MatchPlayer firstSelector;
	private MatchPlayer secondSelector;

	public final static char WILDCARD_CHARACTER = '?';

	public Tile(byte tileId, char letter, Trigger trigger, Board board)
	{
		this.tileId = tileId;
		this.letter = letter;
		this.trigger = trigger;
		this.board = board;
		this.column = 0;
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
	
	public void select(MatchPlayer player)
	{
		
	}
	
	public void deselect(MatchPlayer player)
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
		List<Tile> column = this.board.getColumn(this.column);
		return column.indexOf(this);
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
