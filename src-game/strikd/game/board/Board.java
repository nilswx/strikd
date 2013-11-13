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
    protected final int width;
    protected final int height;
    
    protected final List<Tile>[] columns;
    
	protected final WordDictionary dictionary;
	
	private byte idAllocator;
	private final Map<Byte, Tile> tiles;
	
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

    public List<Tile> getColumn(int x)
    {
        return this.columns[x];
    }
	
	public Tile addTile(int column, char letter)
	{
		return this.addTile(column, letter, null);
	}
	
	public Tile addTile(int column, char letter, Trigger trigger)
	{
		// Factory method for creating Tiles
		Tile tile = this.newTile(this.allocateId(), column, letter, trigger);
		
		// Add to index and grid
		this.tiles.put(tile.getTileId(), tile);
		this.columns[column].add(tile);
		
		// For update message
		this.addedTiles.add(tile);
		System.out.println("added " + tile);
		
		return tile;
	}
	
	protected abstract Tile newTile(byte tileId, int column, char letter, Trigger trigger);
	
	private synchronized byte allocateId()
	{
		do
		{
			if(this.idAllocator++ > Byte.MAX_VALUE)
			{
				this.idAllocator = 1;
			}
		}
		while(this.tiles.containsKey(this.idAllocator));
		
		return this.idAllocator;
	}
	
	public void removeTile(Tile tile)
	{
		// Known tile?
		if(this.tiles.containsKey(tile.getTileId()))
		{
			// Woo
			System.out.println("removed " + tile);
			
			// Cancel selections etc
			tile.remove();
			
			// Remove from index and grid
			this.tiles.remove(tile);
			this.columns[tile.getColumn()].remove(tile);
			
			// For update message
			this.removedTiles.add(tile);
		}
	}
	
	public final void clear()
	{
		for(List<Tile> column : this.columns)
		{
			column.clear();
		}
	}

    public final void clearTiles(List<Tile> tiles)
    {
        for(Tile tile : tiles)
        {
            List<Tile> column = this.columns[tile.getColumn()];
            column.remove(tile.getRow());
        }
    }

	public void rebuild()
	{
		this.clear();
		this.update();
	}
	
	public abstract void update();
	
	public void destroy()
	{
		// Override me
	}
	
	public BoardUpdateMessage generateUpdateMessage()
	{
		BoardUpdateMessage update = new BoardUpdateMessage(this.removedTiles, this.addedTiles);
		this.removedTiles.clear();
		this.addedTiles.clear();
		
		return update;
	}
	
	public final Tile getTile(int x, int y)
	{
		if(x >= 0 && x < this.columns.length)
		{
			if(y >= 0 && y < this.columns[x].size())
			{
				return this.columns[x].get(y);
			}
		}
		
		return null;
	}
	
	public Collection<Tile> getTiles()
	{
		return this.tiles.values();
	}
	
	public Tile getTile(byte tileId)
	{
		return this.tiles.get(tileId);
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

				Tile tile = this.getTile(x, y);
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
		for(int x = 0; x < this.getWidth(); x++)
		{
			sb.append("  ");
			sb.append(x);
			sb.append("  ");
		}
		
		return sb.toString();
	}
}
