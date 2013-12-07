package strikd.game.match.bots.ai;

import static strikd.game.items.ItemTypeRegistry._I;

import java.util.List;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import strikd.communication.outgoing.TileSelectionExtendedMessage;
import strikd.game.board.Board;
import strikd.game.board.Direction8;
import strikd.game.board.Tile;
import strikd.game.items.PowerUp;
import strikd.game.match.SelectionValidator;
import strikd.game.match.bots.MatchBotAI;
import strikd.game.match.bots.MatchBotPlayer;
import strikd.locale.LocaleBundle;
import strikd.locale.StaticLocale;
import strikd.locale.LocaleBundle.DictionaryType;
import strikd.util.RandomUtil;
import strikd.words.WordDictionary;
import strikd.words.index.LetterNode;

public class DefaultMatchBotAI extends MatchBotAI
{
	private static final Logger logger = LoggerFactory.getLogger(DefaultMatchBotAI.class);
	
	private Queue<Tile> toSelect = Lists.newLinkedList();
	
	private final int allowedFindAttempts;
	private final WordDictionary dictionary;
	private final PowerUp[] availablePowerUps;
	
	public DefaultMatchBotAI(MatchBotPlayer player)
	{
		super(player);
		
		// This bot knows all the words!
		LocaleBundle locale = null;//super.getMatch().getLocale();
		this.dictionary = StaticLocale.getDictionary();//locale.getDictionary(DictionaryType.COMPLETE);
		
		// Use max x bruteforce attempts to find a word
		this.allowedFindAttempts = 25;
		
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
		
		// Up & running!
		logger.debug("{}: {} dictionary ({} words)", null/*locale.getLocale()*/, this.dictionary.size());
	}
	
	private boolean hasWord()
	{
		return !this.toSelect.isEmpty();
	}

	@Override
	public void nextMove()
	{
		if(this.hasWord())
		{
			this.selectNextTile();
		}
		else
		{
			if(RandomUtil.getBool(0.7))
			{
				this.pickNewWord();
			}
			else
			{
				this.useRandomPowerUp();
			}
		}
	}

	@Override
	public int nextMoveDelay()
	{
		// Working on a word?
		if(this.hasWord())
		{
			// 1-2 seconds per tile
			return RandomUtil.pickInt(1000, 2000);
		}
		else
		{
			// Max idle time
			int idleTime = RandomUtil.pickInt(1000, 5000);
			logger.debug("{} will be thinking for {} ms", this.getPlayer(), idleTime);
			
			return idleTime;
		}
	}
	
	private boolean pickNewWord()
	{
		// Give it a few tries
		Board board = super.getBoard();
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
					logger.debug("{} will select {} (found in {} tries)", super.getPlayer(), progress, attempt+1);
					return this.toSelect.addAll(progress);
				}
				else
				{
					// Prepare for next search
					progress.clear();
				}
			}
		}
		
		// No word found in this board?
		logger.debug("{} couldn't find jack shit", this.getPlayer());
		return false;
	}
	
	private void selectNextTile()
	{
		// Letters remaining?
		Tile tile = this.toSelect.poll();
		if(tile != null)
		{
			// Select the tile
			MatchBotPlayer self = this.getPlayer();
			self.selectTile(tile);
			self.getOpponent().send(new TileSelectionExtendedMessage(self, ImmutableList.of(tile)));
			
			// Done?
			if(this.toSelect.isEmpty())
			{
				SelectionValidator.validateSelection(self);
			}
		}
	}
	
	private void useRandomPowerUp()
	{
		PowerUp powerUp = RandomUtil.pickOne(this.availablePowerUps);
		if(powerUp != null)
		{
			powerUp.activate(super.getPlayer());
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
