package strikd.game.match.bots.impl;

import static strikd.game.items.ItemTypeRegistry._I;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import strikd.game.board.Board;
import strikd.game.board.Direction8;
import strikd.game.board.Tile;
import strikd.game.items.PowerUp;
import strikd.game.match.SelectionValidator;
import strikd.game.match.bots.MatchBotPlayer;
import strikd.game.player.Experience;
import strikd.game.player.Player;
import strikd.locale.LocaleBundle;
import strikd.locale.LocaleBundle.DictionaryType;
import strikd.util.RandomUtil;
import strikd.words.WordDictionary;
import strikd.words.index.LetterNode;

public class SimpleMatchBotPlayer extends MatchBotPlayer
{
	private static final Logger logger = LoggerFactory.getLogger(SimpleMatchBotPlayer.class);
	
	private int allowedFindAttempts;
	private WordDictionary dictionary;
	private PowerUp[] availablePowerUps;
	
	private final float elitePercentage;
	private final float noobPercentage;
	
	public SimpleMatchBotPlayer(Player bot)
	{
		super(bot);
		
		// How elite am I?
		this.elitePercentage = (float)this.getInfo().getLevel() / (float)Experience.MAX_LEVEL;
		this.noobPercentage = (1 - elitePercentage);
	}
	
	@Override
	protected boolean initializeAI()
	{
		// This bot knows all the words!
		LocaleBundle locale = super.getMatch().getLocale();
		if(locale == null || (this.dictionary = locale.getDictionary(DictionaryType.COMMON)) == null)
		{
			logger.warn("{} couldn't resolve COMMON dict for locale '{}', AI disabled", this, this.getInfo().getLocale());
			return false;
		}
		
		// Use max x bruteforce attempts to find a word
		this.allowedFindAttempts = 75;
				
		// Configure available powerups
		PowerUp[] powerUps =
		{
			(PowerUp)_I("FREEZE"),
			//(PowerUp)_I("SAND"),
			//(PowerUp)_I("WATER"),
			//(PowerUp)_I("EARTHQUAKE"),
			//(PowerUp)_I("SNITCH")
		};
		this.availablePowerUps = powerUps;
		
		// Fertig!
		logger.debug("{}: {} dictionary ({} words), {} powerups",
				this, locale.getLocale(),
				this.dictionary.size(), this.availablePowerUps.length);	
		return true;
	}

	@Override
	protected void nextMove()
	{
		this.selectNextWord();
		/*if(RandomUtil.getBool(0.95))
		{
			this.selectNextWord();
		}
		else
		{
			this.useRandomPowerUp();
		}*/
	}

	@Override
	protected int nextMoveDelay()
	{
		// Player at lvl 1
		final int noobMinDelay = 5000;
		final int noobMaxDelay = 13*1000;
		final int noobVariance = 5000;
		
		// Player at max level
		final int eliteMinDelay = 1000;
		final int eliteMaxDelay = 5000;
		final int eliteVariance = 1000;
		
		// My delays (get shorter when bot has more XP)
		int myMinDelay = Math.max(eliteMinDelay, (int)(noobMinDelay * this.noobPercentage));
		int myMaxDelay = Math.max(eliteMaxDelay, noobMaxDelay - (int)(noobMaxDelay * this.elitePercentage));
		
		// Add some variance
		myMinDelay += (noobVariance * this.noobPercentage);
		myMaxDelay -= (eliteVariance * this.elitePercentage);
		
		// Pick a delay!
		int delay = RandomUtil.pickInt(myMinDelay, myMaxDelay) ;
		logger.debug("{} will do a new move in {} ms", this, delay);
		
		return delay;
	}
	
	private boolean selectNextWord()
	{
		// Get board ref
		Board board = this.getMatch().getBoard();
		
		// Give it a few tries
		List<Tile> progress = Lists.newArrayList();
		for(int attempt = 0; attempt < this.allowedFindAttempts; attempt++)
		{
			// Pick a random start tile
			Tile root = board.getTile(RandomUtil.nextInt(board.getWidth()), RandomUtil.nextInt(board.getHeight()));
			if(root != null)
			{
				// Run new search
				boolean found = findFirstWholeWord(root, null, this.dictionary.getIndex(), progress);
				if(found)
				{
					// Select them!
					logger.debug("{} will select {} (found in {} tries)", this, progress, attempt+1);
					return SelectionValidator.validateSelection(this, progress);
				}
				else
				{
					// Prepare for next search
					progress.clear();
				}
			}
		}
		
		// No word found in this board?
		logger.debug("{} couldn't find jack shit", this.getInfo());
		return false;
	}
	
	protected void useRandomPowerUp()
	{
		PowerUp powerUp = RandomUtil.pickOne(this.availablePowerUps);
		if(powerUp != null)
		{
			powerUp.activate(this);
		}
	}
	
	private static final Direction8[] SEARCH_DIRECTIONS = Direction8.all();
	
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
}
