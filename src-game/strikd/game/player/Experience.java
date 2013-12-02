package strikd.game.player;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import strikd.communication.outgoing.ExperienceMessage;
import strikd.sessions.Session;

public class Experience
{
	public static final int MAX_LEVEL = 45;
	
	private static final int FORMULA_BASE = 100;
	private static final float FORMULA_MULTIPLIER = 1.20f;
	
	private static final int[] LEVEL_EXPERIENCE;
	
	private static final Logger logger = LoggerFactory.getLogger(Experience.class);
	
	static
	{
		// Fill XP cache
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
			System.out.println(String.format("Level %d: %d-%d (%d needed, %d more)", level, levelBegin, levelEnd, levelNeed, (levelNeed - prevNeed)));
			
			// Save for next
			prevNeed = levelNeed;
		}
		System.out.println();
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
	
	public static ExperienceMessage addExperience(Player player, Session session, int points)
	{
		// Impose limits?
		if(points < 0 || player.getLevel() >= MAX_LEVEL)
		{
			points = 0;
		}
		
		// Cache values and determine amount of level-ups
		int currentLevel = player.getLevel(), currentXP = player.getXp();
		int resultLevel = calculateLevel(currentXP + points);
		int levelUps = (resultLevel - currentLevel);
		logger.debug("adding {} xp to {} will cause {} level-ups", points, player.getName(), levelUps);
		
		// Initialize the experience message
		ExperienceMessage msg = new ExperienceMessage(points, levelUps + 1);
		
		do
		{
			// Add XP, but within the current level
			int endXP = getLevelEnd(currentLevel);
			int pointsToNext = (endXP - currentXP);
			int pointsToAdd = (points < pointsToNext || currentLevel == MAX_LEVEL) ? points : pointsToNext;
			currentXP += pointsToAdd;
			
			// Write progress on this level to message
			msg.writeLevel(currentLevel, getLevelBegin(currentLevel), currentXP, endXP);
			
			// Level up?
			if(currentXP == endXP)
			{
				currentLevel++;
				onLevelUp(player, session, currentLevel);
			}
			
			// Added some points!
			points -= pointsToAdd;
		}
		while(points > 0);
		
		// Set final level and XP
		player.setXp(currentXP);
		player.setLevel(currentLevel);
		
		// Return the composed update message
		return msg;
	}
	
	private static void onLevelUp(Player player, Session session, int level)
	{
		// Hurray!
		logger.debug("DING, {} reached {}", player.getName(), level);
		
		// TODO: create event in stream + publish FB story
		
		// TODO: reward with items / coins for level x
	}
	
	public static void main(String[] args)
	{
		Player john = new Player();
		john.setName("John");
		john.setXp(0);
		john.setLevel(calculateLevel(john.getXp()));
	
		addExperience(john, null, 3);
	}
}
