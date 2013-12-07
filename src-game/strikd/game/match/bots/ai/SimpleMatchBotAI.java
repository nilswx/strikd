package strikd.game.match.bots.ai;

import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import strikd.game.board.Board;
import strikd.game.board.Direction8;
import strikd.game.board.Tile;
import strikd.game.match.bots.MatchBotPlayer;
import strikd.locale.LocaleBundle;
import strikd.locale.LocaleBundle.DictionaryType;
import strikd.util.RandomUtil;
import strikd.words.WordDictionary;
import strikd.words.index.LetterNode;

public class SimpleMatchBotAI extends OldMatchBotAI
{
	private static final Logger logger = LoggerFactory.getLogger(SimpleMatchBotAI.class);
	
	private static final Direction8[] SEARCH_DIRECTIONS = Direction8.all();
	
	private final int allowedTries;
	private final WordDictionary knowledge;
	private final List<Tile> progress;
	
	public SimpleMatchBotAI(MatchBotPlayer player)
	{
		super(player);
		
		// 50 searches per tick
		this.allowedTries = 50;
		
		// Reuse progress collector
		this.progress = Lists.newArrayList();

		// This bot knows everything!
		LocaleBundle locale = super.getMatch().getLocale();
		this.knowledge = locale.getDictionary(DictionaryType.COMPLETE);
		 
		logger.debug("{}: {} dictionary ({} words)", locale.getLocale(), this.knowledge.size());
	}

	@Override
	public int getNextMoveDelay()
	{
		int delay = new Random().nextInt(10*1000);
		
		return delay;
	}
	
	@Override
	public List<Tile> nextMove()
	{
		// Give it a few tries
		Board board = super.getBoard();
		for(int i = 0; i < this.allowedTries; i++)
		{
			// Pick a random start tile
			Tile root = board.getTile(RandomUtil.nextInt(board.getWidth()), RandomUtil.nextInt(board.getHeight()));
			if(root != null)
			{
				// Clear previous results
				this.progress.clear();
				
				// Run new search
				boolean found = this.findFirstWholeWord(root, null, this.knowledge.getIndex(), this.progress);
				if(found)
				{
					return this.progress;
				}
			}
		}
		
		return null;
	}
	
	private boolean findFirstWholeWord(Tile src, Direction8 origin, LetterNode letter, List<Tile> progress)
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
							boolean wordComplete = this.findFirstWholeWord(toTry, dir.invert(), currentLetter, progress);
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
}
