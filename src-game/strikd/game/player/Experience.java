package strikd.game.player;

import strikd.sessions.Session;

@SuppressWarnings("unused")
public class Experience
{
	public static final int MAX_LEVEL = 45;
	private static final int FORMULA_BASE = 100;
	private static final float FORMULA_MULTIPLIER = 1.20f;
	
	private static final int[] LEVEL_EXPERIENCE;
	
	static
	{
		// Fill cache
		LEVEL_EXPERIENCE = new int[MAX_LEVEL + 1];
		for(int level = 1; level <= MAX_LEVEL; level++)
		{
			LEVEL_EXPERIENCE[level] = (int)(FORMULA_BASE * Math.pow(FORMULA_MULTIPLIER, level));
		}
		
		// Print cache
		for(int level = 1, prevNeed = 0; level <= MAX_LEVEL; level++)
		{
			// Calculate level boundaries
			int levelBegin = getLevelBegin(level);
			int levelEnd = getLevelEnd(level);
			int levelNeed = (levelEnd - levelBegin);
			//System.out.println(String.format("Level %d: %d-%d (%d needed, %d more)", level, levelBegin, levelEnd, levelNeed, (levelNeed - prevNeed)));
			
			// Save for next
			prevNeed = levelNeed;
		}
	}
	
	public static int calculateLevel(int experience)
	{
		int level = 1;
		while((level + 1) < LEVEL_EXPERIENCE.length && experience >= LEVEL_EXPERIENCE[level + 1 - 1])
		{
			level++;
		}
		
		return level;
	}
	
	public static int calculateNextGoal(int experience)
	{
		return 0; // formula
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
	
	public static void addExperience(Session session, int points)
	{
		// Add points and calculate new level
		Player player = session.getPlayer();
		player.xp += points;
		int newLevel = calculateLevel(player.xp);
		
		// Handle level ups
		if(newLevel != player.level)
		{
			// Process all new levels
			for(int level = player.level; level <= newLevel; level++)
			{
				onLevelUp(session, level);
			}
			player.level = newLevel;
		}
		
		// Save this stuff immediately
		session.saveData();
	}
	
	public static void addExp(Player player, int points)
	{
		// Limit reached?
		if(player.level >= MAX_LEVEL)
		{
			return;
		}
		
		// Calculate goal
		//Player player = session.getPlayer();
		
		// Add all the experience points
		int pointsLeft = points;
		while(pointsLeft > 0)
		{
			// Add XP, but within the current level
			int endXP = getLevelEnd(player.level);
			int pointsToNext = (endXP - player.xp);
			int pointsToAdd = (pointsLeft < pointsToNext || player.level == MAX_LEVEL) ? pointsLeft : pointsToNext;
			player.xp += pointsToAdd;
			
			// Level up?
			if(player.xp == endXP)
			{
				player.level++;
				onLevelUp(null, player.level);
			}
			
			// Added some points!
			pointsLeft -= pointsToAdd;
		}
		
		pointsLeft = 0;
	}
	
	private static void onLevelUp(Session session, int level)
	{
		System.out.println("DING, reached " + level);
		
		// TODO: create event in feeds
		
		// TODO: reward with items for level x
	}
	
	public static void main(String[] args)
	{
		Player john = new Player();
		john.xp = 0;
		john.level = 0;
		addExp(john, Integer.MAX_VALUE);
		
		System.out.println(calculateLevel(Integer.MAX_VALUE - 1));
	}
}
