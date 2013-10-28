package strikd.game.board.impl;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import strikd.game.board.Board;
import strikd.game.board.Square;
import strikd.util.NamedThreadFactory;
import strikd.words.WordDictionary;

public class AgingGravityBoard extends GravityBoard implements Runnable
{
	private static final ScheduledExecutorService agingExecutor = Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory("AgingThread"));

	private final ScheduledFuture<?> aging;
	private final int maxAge;
	
	public AgingGravityBoard(int width, int height, WordDictionary dictionary, int agingDelaySeconds, int maxAge)
	{
		super(width, height, dictionary);
		this.maxAge = maxAge;
		this.aging = agingExecutor.scheduleAtFixedRate(this, agingDelaySeconds, 1, TimeUnit.SECONDS);
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
	public Square newSquare(int x, int y)
	{
		return new AgingSquare(x, y, this);
	}
	
	@Override
	public void run()
	{
		// Age tiles and crush the ones that are too old
		AgingSquare[][] squares = (AgingSquare[][])this.squares;
		for(int x = 0; x < this.getWidth(); x++)
		{
			for(int y = 0; y < this.getHeight(); y++)
			{
				// Tile here, and too old now?
				if(squares[x][y].isTile() && squares[x][y].age() >= this.maxAge)
				{
					// Crush it
					squares[x][y].clear();
				}
			}
		}
		
		// Fill new gaps
		this.fill();
	}
	
	private static class AgingSquare extends Square
	{
		private int age;
		
		public AgingSquare(int x, int y, Board board)
		{
			super(x, y, board);
		}
		
		public int age()
		{
			return (++this.age);
		}
		
		public void resetAge()
		{
			this.age = 0;
		}
		
		@Override
		public void setLetter(char letter)
		{
			super.setLetter(letter);
			this.resetAge();
		}
		
		@Override
		public void clear()
		{
			super.clear();
			this.resetAge();
		}
	}
}
