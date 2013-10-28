package strikd.game.match.bots.ai;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;

import strikd.game.board.Board;
import strikd.game.board.Direction8;
import strikd.game.board.Square;
import strikd.game.board.StaticLocale;
import strikd.game.board.impl.BruteBoard;
import strikd.words.WordDictionary;
import strikd.words.index.LetterNode;

public class IndexedWordFinder
{
	private static Direction8[] SEARCH_DIRECTIONS = Direction8.all();
	
	public static void main2(String[] args)
	{
		// The dictionary
		WordDictionary dict = StaticLocale.getDictionary();
		
		// The board
		Board board = new BruteBoard(5, 5, dict);
		board.regenerate();
		
		// The random start tile
		Random rand = new Random();
		Square root = board.getSquare(rand.nextInt(board.getWidth()), rand.nextInt(board.getHeight()));
		
		// Ready...
		Stopwatch sw = new Stopwatch();
		List<Square> progress = Lists.newArrayList();
		
		// Go!
		sw.start();
		boolean found = findWord(root, null, dict.getIndex(), progress);
		sw.stop();
		
		// Success?
		if(found)
		{
			System.out.println(board.toMatrixString());
			printProgress(progress);
		}
		else
		{
			System.out.println("nothing found, start somewhere else");
		}
		
		// Print time taken
		System.out.println(String.format("%d ms (%d microseconds)", sw.elapsedMillis(), sw.elapsedTime(TimeUnit.MICROSECONDS)));
	}
	
	public static void main(String[] args)
	{
		// The dictionary
		WordDictionary dict = StaticLocale.getDictionary();
		
		// The board
		Board board = new BruteBoard(20, 20, dict);
		board.regenerate();
		
		// The progress holders
		List<Square> progress = Lists.newArrayList();
		
		// Keep finding
		Stopwatch sw = new Stopwatch();

		// Random source
		Random rand = new Random();
		
		// Find a ton of words
		for(int word = 0; word < 50; word++)
		{
			// Clear progress
			progress.clear();
			
			// The random start tile
			Square root = board.getSquare(rand.nextInt(board.getWidth()), rand.nextInt(board.getHeight()));
			if(root.isTile())
			{
				// Run new search
				sw.reset();
				sw.start();
				boolean found = findWord(root, null, dict.getIndex(), progress);
				sw.stop();
				
				// Success?
				if(found)
				{
					printProgress(progress);
					//System.out.println(board.toMatrixString());
					System.out.println(String.format("%d ms (%d microseconds)", sw.elapsedMillis(), sw.elapsedTime(TimeUnit.MICROSECONDS)));
					System.out.println();
				}
				else
				{
					//System.out.println("Nothing found, start somewhere else");
				}
			}
		}
	}
	
	private static boolean findWord(Square src, Direction8 origin, LetterNode letter, List<Square> progress)
	{
		// This link is in the dictionary?
		LetterNode currentLetter = letter.node(src.getLetter());
		if(currentLetter != null)
		{
			// Okay, try continuing with this letter
			progress.add(src);
			if(currentLetter.isWord())
			{
				return true;
			}
			else
			{
				// Now search for another linking letter in all allowed directions
				for(Direction8 dir : SEARCH_DIRECTIONS)
				{
					// But do not go back already
					if(dir != origin)
					{
						// Tile here?
						Square toTry = src.getBoard().getSquare(src.x + dir.x, src.y + dir.y);
						if(toTry != null && toTry.isTile())
						{
							// Try it, stop on success
							boolean wordComplete = findWord(src.getBoard().getSquare(src.x + dir.x, src.y + dir.y), dir.invert(), currentLetter, progress);
							if(wordComplete)
							{
								return true;
							}
						}
					}
				}
			}
			
			// No luck with this tile, go back
			progress.remove(progress.size() - 1);
		}
		
		// Got a word so far?
		return letter.isWord();
	}
	
	private static void printProgress(List<Square> progress)
	{
		System.out.print("found: ");
		for(Square x : progress)
		{
			System.out.print(x.getLetter());
		}
		System.out.println();
		
		for(Square x : progress)
		{
			//System.out.println(x);
		}
	}
}
