package strikd.game.achievements;

import java.util.Map;

import strikd.Server;

public class AchievementManager extends Server.Referent
{
	private Map<Integer, Achievement> achievementsById;
	private Map<String, Achievement> achievementsByCode;
	
	public AchievementManager(Server server)
	{
		super(server);
	}
}
