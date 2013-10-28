package strikd.game.board.impl;

import java.awt.Point;
import java.io.IOException;

import strikd.game.board.Board;
import strikd.game.board.Direction8;
import strikd.game.board.StaticLocale;
import strikd.words.Word;
import strikd.words.WordDictionary;

public class GappieBoard extends Board
{
	public GappieBoard(int width, int height, WordDictionary dictionary)
	{
		super(width, height, dictionary);
	}
	
	@Override
	public void fill()
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
				if(this.getSquare(x, y).isNull())
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
		Word word = this.dictionary.pickOne();
		char[] letters = word.letters();

		// Mingle the letters through the board
		int x = root.x, y = root.y;
		for(int i = 0; i < letters.length; i++)
		{
			// Fill this square with a tile for this one
			this.squares[x][y].setLetter(letters[i]);

			// Get random direction
			boolean dirOK = false;
			do
			{
				Direction8 next = Direction8.random();

				// Apply diff
				int testX = (x + next.x);
				int testY = (y + next.y);

				if(this.squareExists(testX, testY) /* && this.squares[testX][testY].isNull()*/)
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
		long start = System.currentTimeMillis();
		Board board = new GappieBoard(5, 6, StaticLocale.getDictionary());
		board.regenerate();
		long time = System.currentTimeMillis() - start;

		System.out.println(board.toString() + " => " + time + " ms");
		System.out.println();
		System.out.println(board.toMatrixString());
	}
}
