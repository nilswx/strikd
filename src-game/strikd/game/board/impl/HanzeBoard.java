package strikd.game.board.impl;

import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

import org.apache.log4j.Logger;

import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;

import strikd.game.board.Board;
import strikd.game.board.Direction8;
import strikd.game.board.FutureTile;
import strikd.game.board.Square;
import strikd.game.board.Tile;
import strikd.game.board.triggers.Trigger;
import strikd.util.RandomUtil;
import strikd.words.WordDictionary;
import strikd.words.index.LetterNode;

public class HanzeBoard extends Board
{
	private static final Logger logger = Logger.getLogger(HanzeBoard.class);

	private Queue<Square> emptySquares;

	public HanzeBoard(int width, int height, WordDictionary dictionary)
	{
		super(width, height, dictionary);
	}
	
	private void findEmptySquares()
	{
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
		Collections.shuffle(empty);
		this.emptySquares = Lists.newLinkedList(empty);
		
		logger.debug("empty=" + this.emptySquares.size());
	}

	@Override
	public void update()
	{
		// Entering!
		logger.debug("begin update()");

		// Find empty stuff (ugh!)
		this.findEmptySquares();
		
		// Anything to fill?
		Square start;
		while((start = this.emptySquares.poll()) != null)
		{
			// Determine the best placement
			WordPlacement bestPlacement = null;//Ordering.natural().max(this.generatePlacements(start, 10));
			if(bestPlacement == null)
			{
				this.addTile(start.column, RandomUtil.generateRandomLetter());
			}
			else
			{
				// Place the tiles in these squares
				for(FutureTile tile : bestPlacement)
				{
					this.addTile(tile.column, tile.letter);
				}
				
				System.out.println(this.toMatrixString());
				break;
			}
		}
	}

	private char[] getRandomizedAlphabet()
	{
		return RandomUtil.randomizeString("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
	}

	private List<WordPlacement> generatePlacements(Square start, int amount)
	{
		List<WordPlacement> found = Lists.newArrayList();
		while(found.size() < amount)
		{
			// Run recursion
			Stack<FutureTile> progress = new Stack<FutureTile>();
			char[][] wildcards = new char[this.width][this.height];
			this.findWordStartingFrom(start, this.dictionary.getIndex(), null, progress, wildcards);

			// Result?
			if(!progress.isEmpty())
			{
				found.add(new WordPlacement(progress));
			}
		}

		return found;
	}

	private boolean findWordStartingFrom(Square cur, LetterNode branch, Direction8 origin, Stack<FutureTile> progress, char[][] wildcards)
	{
		Tile tile = this.getTile(cur.column, cur.row);

		char letter = (tile != null ? tile.getLetter() : wildcards[cur.column][cur.row]);
		if(letter == 0)
		{
			// Wildcard!
			for(char randLetter : this.getRandomizedAlphabet())
			{
				// Letter from here?
				LetterNode node = branch.node(randLetter);
				if(node != null)
				{
					wildcards[cur.column][cur.row] = randLetter;
					return this.findWordStartingFrom(cur, branch, origin, progress, wildcards);
				}
			}
		}
		else
		{
			LetterNode myBraansj = branch.node(letter);

			if(myBraansj != null)
			{
				FutureTile ft = new FutureTile(cur.column, cur.row, letter);

				// Keep track of progress
				progress.push(ft);

				if(myBraansj.isWordEnd())
				{
					return true;
				}

				// Maybe there are more, continue searching
				for(Direction8 direction : SEARCH_DIRECTIONS)
				{
					if(direction != origin)
					{
						// Try to get next tile at new direction
						Square next = new Square(cur.column + direction.x, cur.row + direction.y);
						if(next.column >= 0&& next.column < this.width && next.row >= 0 && next.row < this.height)
											// !this.visitedSquarePosition(nextSquare,
											// progressStack))
						{
							this.findWordStartingFrom(next, myBraansj, direction.invert(), progress, wildcards);
						}
					}
				}
				
				progress.pop();
			}
		}
		
		return false;
	}

	private static final Direction8[] SEARCH_DIRECTIONS =
	{ Direction8.North, Direction8.East, Direction8.South, Direction8.West };

	@Override
	protected Tile newTile(byte tileId, int column, char letter, Trigger trigger)
	{
		return new Tile(tileId, column, letter, trigger, this);
	}
}
