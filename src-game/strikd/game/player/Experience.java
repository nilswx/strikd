package strikd.game.player;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import strikd.communication.outgoing.LevelsMessage;

/**
 * Holds experience amounts per level.
 */
public final class Experience
{
	public static final int MAX_LEVEL = 25;
	
	private static final int FORMULA_BASE = 100;
	private static final float FORMULA_MULTIPLIER = 1 + (0.20f * (45/MAX_LEVEL));
	private static final int[] LEVEL_EXPERIENCE;
	
	private static final LevelsMessage levelsMessage;
	
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
			LEVEL_EXPERIENCE[level] = (int)(FORMULA_BASE * Math.pow(FORMULA_MULTIPLIER, level));
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
}
