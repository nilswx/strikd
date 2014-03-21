package strikd.game.player;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import strikd.communication.outgoing.LevelsMessage;

/**
 * Holds experience amounts per level.
 */
public final class Experience
{
	// Level Cap
	public static final int MAX_LEVEL = 20;
	
	// Formula and growth
	private static final int FORMULA_BASE = 75;
	private static final float GROW_PERCENTAGE = 0.3f;
	
	// Caches
	private static final int[] LEVEL_EXPERIENCE;
	private static final LevelsMessage levelsMessage;
	
	// Logger
	private static final Logger logger = LoggerFactory.getLogger(Experience.class);
		
	public static int calculateLevel(int experience)
	{
		int level = 1;
		while((level + 1) < LEVEL_EXPERIENCE.length && experience >= LEVEL_EXPERIENCE[level + 1 - 1])
		{
			level++;
		}
		
		return level;
	}
	
	public static int getLevelBegin(int level)
	{
		if(level >= 0 && level <= MAX_LEVEL)
		{
			return LEVEL_EXPERIENCE[level - 1];
		}
		else
		{
			return 0;
		}
	}
	
	public static int getLevelEnd(int level)
	{
		if(level > 0 && level < MAX_LEVEL)
		{
			return getLevelBegin(level + 1);
		}
		else
		{
			return 0;
		}
	}
	
	public static LevelsMessage getLevelsMessage()
	{
		return levelsMessage;
	}
	
	static
	{
		// Fill XP cache
		LEVEL_EXPERIENCE = new int[MAX_LEVEL + 1];
		for(int level = 1; level <= MAX_LEVEL; level++)
		{
			int prev = LEVEL_EXPERIENCE[level - 1];
			int prevNeed = (level > 1) ? (prev - LEVEL_EXPERIENCE[level - 2]) : 0;
			int newNeed = (int) (prevNeed * (1 + GROW_PERCENTAGE));
			
			LEVEL_EXPERIENCE[level] = FORMULA_BASE + prev + newNeed;
		}
		
		
		// Pre=compute message
		levelsMessage = new LevelsMessage(LEVEL_EXPERIENCE);
		
		// Print cache
		logger.info("defined {} levels, message size: {} bytes", MAX_LEVEL, levelsMessage.length());
		for(int level = 1, prevNeed = 0; level <= MAX_LEVEL; level++)
		{
			// Calculate level boundaries
			int levelBegin = getLevelBegin(level);
			int levelEnd = getLevelEnd(level);
			int levelNeed = (levelEnd - levelBegin);
			logger.debug("Level {}: {}-{} ({} needed, {} more)", level, levelBegin, levelEnd, levelNeed, (levelNeed - prevNeed));
			
			// Save for next
			prevNeed = levelNeed;
		}
	}
	
	public static void main(String[] args)
	{
		Experience.class.toString();
	}
}
