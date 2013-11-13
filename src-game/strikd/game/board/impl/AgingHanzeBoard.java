package strikd.game.board.impl;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import strikd.game.board.Board;
import strikd.game.board.StaticLocale;
import strikd.game.board.Tile;
import strikd.game.board.triggers.Trigger;
import strikd.util.NamedThreadFactory;
import strikd.words.WordDictionary;

public class AgingHanzeBoard extends HanzeBoard implements Runnable
{
	private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory("AgingThread"));

	private final ScheduledFuture<?> aging;
	private final int maxAge;
	
	public AgingHanzeBoard(int width, int height, WordDictionary dictionary, int agingDelaySeconds, int maxAge)
	{
		super(width, height, dictionary);
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
		return new AgingTile(tileId, column, letter, trigger, this);
	}
	
	@Override
	public void run()
	{
		// Age tiles and crush the ones that are too old
		int expired = 0;
		for(Tile tile : this.getTiles())
		{
			if(((AgingTile)tile).age() >= this.maxAge)
			{
				expired++;
				this.removeTile(tile);
			}
		}
		
		// Fill new gaps
		if(expired > 0)
		{
			this.update();
			// TODO: broadcast updates
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
	
	public static void main(String[] args)
	{
		Board board = new AgingHanzeBoard(5, 5, StaticLocale.getDictionary(), 0, 5);
		board.rebuild();
	}
}