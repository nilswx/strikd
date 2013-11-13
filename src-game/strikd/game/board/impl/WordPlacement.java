package strikd.game.board.impl;

import java.util.ArrayList;
import java.util.List;

import strikd.game.board.Direction8;

@SuppressWarnings("serial")
public class WordPlacement extends ArrayList<FutureTile> implements Comparable<WordPlacement>
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
