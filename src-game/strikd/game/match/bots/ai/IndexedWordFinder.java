package strikd.game.match.bots.ai;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;

import strikd.game.board.Board;
import strikd.game.board.Direction8;
import strikd.game.board.Tile;
import strikd.game.board.impl.RenegadeBoard;
import strikd.locale.StaticLocale;
import strikd.words.WordDictionary;
import strikd.words.index.LetterNode;

public class IndexedWordFinder
{
	private static final Direction8[] SEARCH_DIRECTIONS = Direction8.all();
	
	public static void main(String[] args)
	{
		// The dictionary
		WordDictionary dict = StaticLocale.getDictionary();
		
		// The board
		Board board = new RenegadeBoard(20, 20, dict);
		board.rebuild();
		
		// The progress holders
		List<Tile> progress = Lists.newArrayList();
		
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
			Tile root = board.getTile(rand.nextInt(board.getWidth()), rand.nextInt(board.getHeight()));
			if(root != null)
			{
				// Run new search
				sw.reset();
				sw.start();
				boolean found = findFirstWholeWord(root, null, dict.getIndex(), progress);
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
	
	private static boolean findFirstWholeWord(Tile src, Direction8 origin, LetterNode letter, List<Tile> progress)
	{
		// This link is in the dictionary?
		LetterNode currentLetter = letter.node(src.getLetter());
		if(currentLetter != null)
		{
			// Okay, try continuing with this letter
			progress.add(src);
			if(currentLetter.isWordEnd())
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
						Tile toTry = src.getBoard().getTile(src.getColumn() + dir.x, src.getRow() + dir.y);
						if(toTry != null)
						{
							// Try it, stop on success
							boolean wordComplete = findFirstWholeWord(toTry, dir.invert(), currentLetter, progress);
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
		return letter.isWordEnd();
	}
	
	private static void printProgress(List<Tile> progress)
	{
		System.out.print("found: ");
		for(Tile x : progress)
		{
			System.out.print(x.getLetter());
		}
		System.out.println();
	}
}