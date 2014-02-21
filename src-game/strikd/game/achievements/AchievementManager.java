package strikd.game.achievements;

import java.util.Collection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import strikd.Server;

public class AchievementManager extends Server.Referent
{
	private static final Logger logger = LoggerFactory.getLogger(AchievementManager.class);
	
	private Map<String, Achievement> achievements;
	
	public AchievementManager(Server server)
	{
		super(server);
		
		this.reload();
	}
	
	public void reload()
	{
		// Seed if needed
		this.seedAchievements();
		
		// Load and map by achievement code
		this.achievements = super.getDatabase().find(Achievement.class).findMap("code", String.class);
		
		// Log stats
		logger.info("{} known achievements", this.achievements.size());
	}
	
	private void seedAchievements()
	{
		// Already seeded?
		if(super.getDatabase().find(Achievement.class).findRowCount() > 0)
		{
			return;
		}
		
		// Some testing achievements
		super.getDatabase().save(new Achievement(1, "FIND_STRIK"));
		super.getDatabase().save(new Achievement(2, "WINS_5", 5));
		super.getDatabase().save(new Achievement(3, "WINS_10", 10));
		super.getDatabase().save(new Achievement(4, "WIN_FRIEND"));
		super.getDatabase().save(new Achievement(5, "INVITE_FRIEND"));
		super.getDatabase().save(new Achievement(6, "BATTERY"));
	}
	
	public Collection<Achievement> getAchievements()
	{
		return this.achievements.values();
	}
}
