package strikd.game.board;

import static strikd.game.board.StaticLocale.dict;

import java.awt.Point;
import java.io.IOException;

import strikd.game.board.tiles.Tile;
import strikd.words.Word;

public class GappieBoard extends Board
{
	public GappieBoard(int width, int height)
	{
		super(width, height);
	}

	@Override
	public void regenerate()
	{
		super.clear();
		this.fillGaps();
	}

	public void fillGaps()
	{
		Point gap;
		while((gap = this.findGap()) != null)
		{
			this.fillGap(gap);
		}
	}

	private Point findGap()
	{
		for(int x = 0; x < this.getWidth(); x++)
		{
			for(int y = 0; y < this.getHeight(); y++)
			{
				if(this.tiles[x][y] == null)
				{
					return new Point(x, y);
				}
			}
		}

		return null;
	}

	private void fillGap(Point root)
	{
		// Pick a random word and get the letters
		Word word = dict.pickOne();
		char[] letters = word.letters();

		// Mingle the letters through the board
		int x = root.x, y = root.y;
		for(int i = 0; i < letters.length; i++)
		{
			// Fill this square with a tile for this one
			this.tiles[x][y] = new Tile(x, y, letters[i]);

			// Get random direction
			boolean dirOK = false;
			do
			{
				Direction8 next = Direction8.random();
				Point diff = next.getDiff();

				// Apply diff
				int testX = (x + diff.x);
				int testY = (y + diff.y);

				if(this.squareExists(testX, testY) /*
													 * && this.getTile(testX,
													 * testY) == null
													 */)
				{
					dirOK = true;
					x = testX;
					y = testY;
				}
			}
			while(!dirOK);
		}

		System.out.println("Added " + word);
	}

	public static void main(String[] args) throws IOException
	{
		StaticLocale.init();
		
		long start = System.currentTimeMillis();
		Board board = new GappieBoard(5, 5);
		board.regenerate();
		long time = System.currentTimeMillis() - start;

		System.out.println(board.toString() + " => " + time + " ms");
		System.out.println();
		System.out.println(board.toLongString());
	}
}
