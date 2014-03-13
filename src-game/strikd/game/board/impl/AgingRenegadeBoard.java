package strikd.game.board.impl;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import strikd.game.board.Board;
import strikd.game.board.Tile;
import strikd.game.board.triggers.Trigger;
import strikd.game.match.Match;
import strikd.util.NamedThreadFactory;
import strikd.words.WordDictionary;

public final class AgingRenegadeBoard extends RenegadeBoard implements Runnable
{
	private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory("AgingThread"));

	private final Match match;
	private final ScheduledFuture<?> aging;
	private final int maxAge;
	
	public AgingRenegadeBoard(int width, int height, WordDictionary dictionary, Match match, int agingDelaySeconds, int maxAge)
	{
		super(width, height, dictionary);
		this.match = match;
		this.maxAge = maxAge;
		this.aging = scheduler.scheduleAtFixedRate(this, agingDelaySeconds, 1, TimeUnit.SECONDS);
	}
	
	@Override
	public void destroy()
	{
		if(this.aging != null)
		{
			this.aging.cancel(false);
		}
	}
	
	@Override
	protected Tile newTile(byte tileId, int column, char letter, Trigger trigger)
	{
		// Special type of Tile is used in this board
		return new AgingTile(tileId, column, letter, trigger, this);
	}
	
	@Override
	public void run()
	{
		// Age tiles and crush the ones that are too ol
		/*for(Tile tile : this.getTiles())
		{
			if(((AgingTile)tile).age() >= this.maxAge)
			{
				expired++;
				tile.remove();
			}
		}*/
		
		// Only age tiles in the bottom row (TODO: open a little bottom 'luikje' every x seconds?)
		int expired = 0;
		for(List<Tile> col : this.getColumns())
		{
			// Tile here?
			if(!col.isEmpty())
			{
				// Age 'n check it
				AgingTile tile = (AgingTile)col.get(0);
				if(tile.age() >= this.maxAge)
				{
					expired++;
					tile.destroy();
				}
			}
		}
		
		// Board needs updating?
		if(expired > 0)
		{
			// Broadcast the updates?
			this.update();
			if(this.match == null)
			{
				this.clearUpdates();
			}
			else
			{
				this.match.broadcast(this.generateUpdateMessage());
			}
		}
	}
	
	private static class AgingTile extends Tile
	{
		private int age;
		
		public AgingTile(byte tileId, int column, char letter, Trigger trigger, Board board)
		{
			super(tileId, column, letter, trigger, board);
		}
		
		public int age()
		{
			return (++this.age);
		}
	}
}