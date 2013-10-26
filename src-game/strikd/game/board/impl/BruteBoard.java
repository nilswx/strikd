package strikd.game.board.impl;

import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import strikd.game.board.Board;
import strikd.game.board.Direction8;
import strikd.game.board.StaticLocale;
import strikd.words.Word;
import strikd.words.WordDictionary;

public class BruteBoard extends Board
{
	public BruteBoard(int width, int height, WordDictionary dictionary)
	{
		super(width, height, dictionary);
	}
	
	@Override
	public void fill()
	{
		// Reusable resources
		Random rand = new Random();
		List<Word> triedWords = new ArrayList<Word>();
		List<Point> triedRoots = new ArrayList<Point>(this.getWidth() * this.getHeight());
		List<Direction8> triedDirs = new ArrayList<Direction8>(8);
		
		// 1. Pick a random word from the list
		nextWord: while(this.hasGap())
		{
			// Get an untried random word
			Word word = this.dictionary.pickOne();
			if(triedWords.contains(word))
			{
				continue nextWord;
			}
			else
			{
				// Tried all words?
				triedWords.add(word);
				if(triedWords.size() == this.dictionary.size())
				{
					break nextWord;
				}
			}
			
			// 2. Pick a random cell on the grid
			triedRoots.clear();
			nextRoot: do
			{
				// Get an untried random root
				Point root = new Point(rand.nextInt(this.getWidth()), rand.nextInt(this.getHeight()));
				if(triedRoots.contains(root))
				{
					continue nextRoot;
				}
				
				// 3. Pick a random direction to have the word oriented
				triedDirs.clear();
				nextDirection: do
				{
					// Get an untried random direction
					Direction8 dir = Direction8.random();
					if(triedDirs.contains(dir))
					{
						continue nextDirection;
					}
					
					// 4. See if the word will fit into the grid starting from that cell and running in that direction.
					Point diff = dir.getDiff();
					char[] letters = word.letters();
					for(int step = 0; step < letters.length; step++)
					{
						Point next = new Point(root.x + (step * diff.x), root.y + (step * diff.y));
						
						// Square doesn't exist at all?
						if(!this.squareExists(next.x, next.y)
								
						// Or it does exist, and the letter on it is different?
						|| (this.squares[next.x][next.y].isTile() && this.squares[next.x][next.y].getLetter() != letters[step]))
						{
							triedDirs.add(dir);
							continue nextDirection;
						}
					}
					
					// Put the letters
					for(int step = 0; step < letters.length; step++)
					{
						Point next = new Point(root.x + (step * diff.x), root.y + (step * diff.y));
						this.squares[next.x][next.y].setLetter(letters[step]);
					}
					
					// Yay!
					System.out.println("Added " + word);
					continue nextWord;
				}
				while(triedDirs.size() < 8);
				
				// Tried all directions for this root
				triedRoots.add(root);
				continue nextRoot;
			}
			while(triedRoots.size() < (this.getWidth() * this.getHeight()));
			
			// Give up on this word!
			continue nextWord;
		}
	}
	
	private static final Direction8[] GAP_LINK_DIRS = new Direction8[] { Direction8.North, Direction8.East, Direction8.South, Direction8.West };
	
	private boolean hasGap()
	{
		for(int x = 0; x < this.getWidth(); x++)
		{
			for(int y = 0; y < this.getHeight(); y++)
			{
				if(this.squares[x][y].isNull())
				{
					for(Direction8 dir : GAP_LINK_DIRS)
					{
						Point diff = dir.getDiff();
						if(this.squareExists(x + diff.x, y + diff.y) && this.squares[x + diff.x][y + diff.y].isNull())
						{
							return true;
						}
					}
				}
			}
		}
		
		return false;
	}
	
	public static void main(String[] args) throws IOException
	{
		long start = System.currentTimeMillis();
		Board board = new BruteBoard(5, 6, StaticLocale.dict);
		board.regenerate();
		long time = System.currentTimeMillis() - start;
		
		System.out.println(board.toString() + " => " + time + " ms");
		System.out.println();
		System.out.println(board.toMatrixString());
	}
}
