package strikd.game.board;

import strikd.communication.outgoing.BoardUpdateMessage;
import strikd.game.board.triggers.Trigger;
import strikd.words.WordDictionary;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public abstract class Board
{
	private final int width;
	private final int height;

	private final WordDictionary dictionary;

	private byte tileIdAllocator;
	private final Map<Byte, Tile> tiles;
	private final List<Tile>[] columns;

	private List<Tile> addedTiles;
	private List<Tile> removedTiles;

	@SuppressWarnings("unchecked")
	protected Board(int width, int height, WordDictionary dictionary)
	{
		this.width = width;
		this.height = height;

		// Create index and grid
		this.tiles = Maps.newHashMapWithExpectedSize(width * height);
		this.columns = new List[width];
		for(int x = 0; x < width; x++)
		{
			this.columns[x] = new ArrayList<Tile>();
		}

		this.dictionary = dictionary;

		// Reusable collections for generating update messages
		this.addedTiles = Lists.newArrayList();
		this.removedTiles = Lists.newArrayList();
	}

	public void destroy()
	{
		// Override me
	}
	
	
	public Tile addTile(int column, char letter)
	{
		Trigger trigger = null; // TODO: randomly generate triggers
		return this.addTile(column, letter, trigger);
	}

	public final Tile addTile(int column, char letter, Trigger trigger)
	{
		// Factory method for creating Tiles
		Tile tile = this.newTile(this.allocateId(), column, letter, trigger);

		// Add to index and grid
		this.tiles.put(tile.getTileId(), tile);
		this.columns[column].add(tile);

		// For update message
		this.addedTiles.add(tile);

		return tile;
	}
	
	private final synchronized byte allocateId()
	{
		do
		{
			if(this.tileIdAllocator++ > Byte.MAX_VALUE)
			{
				this.tileIdAllocator = 1;
			}
		}
		while(this.tiles.containsKey(this.tileIdAllocator));

		return this.tileIdAllocator;
	}

	protected Tile newTile(byte tileId, int column, char letter, Trigger trigger)
	{
		return new Tile(tileId, column, letter, trigger, this);
	}

	public void removeTile(Tile tile)
	{
		// Known tile?
		if(this.tiles.containsKey(tile.getTileId()))
		{
			// Cancel selections etc
			//tile.remove();

			// Remove from index and grid
			this.tiles.remove(tile);
			this.columns[tile.getColumn()].remove(tile);

			// For update message
			this.removedTiles.add(tile);
		}
	}
	
	public final BoardUpdateMessage generateUpdateMessage()
	{
		BoardUpdateMessage update = new BoardUpdateMessage(this.removedTiles, this.addedTiles);
		this.clearUpdates();

		return update;
	}

	public final void clearUpdates()
	{
		this.removedTiles.clear();
		this.addedTiles.clear();
	}
	
	public final void clear()
	{
		for(List<Tile> column : this.columns)
		{
			column.clear();
		}
	}

	public void rebuild()
	{
		this.clear();
		this.update();
	}

	public abstract void update();



	public final Tile getTile(int row, int column)
	{
		if(column >= 0 && column < this.columns.length)
		{
			if(row >= 0 && row < this.columns[column].size())
			{
				return this.columns[column].get(row);
			}
		}

		return null;
	}

	public final Tile getTile(byte tileId)
	{
		return this.tiles.get(tileId);
	}
	
	public final Collection<Tile> getTiles()
	{
		return this.tiles.values();
	}

	
	public final int getWidth()
	{
		return this.width;
	}

	public final int getHeight()
	{
		return this.height;
	}

	public final List<Tile> getColumn(int column)
	{
		return this.columns[column];
	}

	public final WordDictionary getDictionary()
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

		for(int row = this.getHeight() - 1; row >= 0; row--)
		{
			sb.append(row);
			sb.append("  ");
			for(int column = 0; column < this.getWidth(); column++)
			{
				sb.append('[');

				Tile tile = this.getTile(row, column);
				if(tile == null)
				{
					sb.append("   ");
				}
				else
				{
					if(!tile.hasTrigger())
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

		sb.append("   ");
		for(int column = 0; column < this.getWidth(); column++)
		{
			sb.append("  ");
			sb.append(column);
			sb.append("  ");
		}

		return sb.toString();
	}
}
