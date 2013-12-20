package strikd.game.board.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;

import com.google.common.collect.Lists;

import strikd.game.board.Board;
import strikd.game.board.Direction8;
import strikd.game.board.Square;
import strikd.game.board.Tile;
import strikd.util.RandomUtil;
import strikd.words.WordDictionary;

public class RenegadeBoard extends Board
{
	private final Queue<Square> emptySquares;
	
	public RenegadeBoard(int width, int height, WordDictionary dictionary)
	{
		super(width, height, dictionary);
		
		// Find all empty squares
		List<Square> empty = Lists.newArrayList();
		for(int column = 0; column < width; column++)
		{
			for(int row = 0; row < height; row++)
			{
				if(this.getTile(column, row) == null)
				{
					empty.add(new Square(column, row));
				}
			}
		}
		
		// We don't want the first update() to be predictable
		Collections.shuffle(empty);
		this.emptySquares = Lists.newLinkedList(empty);
	}
	
	@Override
	public void removeTile(Tile tile)
	{
		this.emptySquares.add(new Square(tile.getColumn(), tile.getRow()));
		
		super.removeTile(tile);
	}

	@Override
	public void update()
	{
		// Spawn new words, starting for all empty squares
		Square start; while((start = this.emptySquares.poll()) != null)
		{
			// Placement starting here?
			WordPlacement placement = this.generateBestPlacement(start);
			if(placement == null)
			{
				// Plug it with something random...
				this.addTile(start.column, RandomUtil.generateRandomLetter());
			}
			else
			{
				// Plug the tiles!
				for(FutureTile tile : placement)
				{
					this.addTile(tile.column, tile.letter);
					this.emptySquares.remove(tile);
				}
			}
		}
	}

	private WordPlacement generateBestPlacement(Square start)
	{
		return null;
	}
	
	public static class FutureTile extends Square
	{
		public final char letter;

		public FutureTile(int column, int row, char letter)
		{
			super(column, row);
			this.letter = letter;
		}

		public String toString()
		{
			return Character.toString(letter);
		}
	}
	
	private static class WordPlacement extends ArrayList<FutureTile> implements Comparable<WordPlacement>
	{
		private static final int LENGTH_SCORE = -5;

		private static final int NORTH_SCORE = -2;
		private static final int EAST_SCORE = 4;
		private static final int SOUTH_SCORE = 3;
		private static final int WEST_SCORE = -2;

		private static final int SAME_DIRECTION_SCORE = 4;

		private final int score;

		public WordPlacement(List<FutureTile> tiles)
		{
			super(tiles);
			this.score = this.calculateScore();
		}

		private int calculateScore()
		{
			// Score for lengths
			int score = this.size() * LENGTH_SCORE;
			for(int i = 0; i < this.size(); i++)
			{
				FutureTile tile = this.get(i);

				// Detect the changes in direction
				if(i > 0)
				{
					// Points for delta
					FutureTile lastSquare = this.get(i - 1);
					int deltaX = tile.column - lastSquare.column;
					int deltaY = tile.row - lastSquare.row;

					// Todo: Improve this
					Direction8 direction = Direction8.directionFromDelta(deltaX, deltaY);
					if(direction == Direction8.North)
					{
						score += NORTH_SCORE;
					}
					else if(direction == Direction8.East)
					{
						score += EAST_SCORE;
					}
					else if(direction == Direction8.South)
					{
						score += SOUTH_SCORE;
					}
					else if(direction == Direction8.West)
					{
						score += WEST_SCORE;
					}

					// Points for keeping in the same direction
					if(i > 1)
					{
						FutureTile beforeLastSquare = this.get(i - 2);
						if((tile.column == lastSquare.column && lastSquare.column == beforeLastSquare.column) || (tile.row == lastSquare.row && lastSquare.row == beforeLastSquare.row))
						{
							score += SAME_DIRECTION_SCORE;
						}
					}
				}
			}

			return score;
		}

		@Override
		public int compareTo(WordPlacement o)
		{
			return o.score - this.score;
		}
	}

}
