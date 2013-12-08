package strikd.game.player;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import strikd.Server;
import strikd.communication.outgoing.ExperienceAddedMessage;
import strikd.game.facebook.LevelReachedStory;
import strikd.game.match.MatchPlayer;
import strikd.game.match.bots.MatchBotPlayer;
import strikd.game.stream.activity.LevelUpStreamItem;
import strikd.sessions.Session;

public class ExperienceHandler extends Server.Referent
{
	private static final Logger logger = LoggerFactory.getLogger(ExperienceHandler.class);
	
	public ExperienceHandler(Server server)
	{
		super(server);
	}
	
	public void addExperience(Player player, int points)
	{
		// Can gain XP?
		if(points > 0 && player.getLevel() < Experience.MAX_LEVEL)
		{
			// Add XP
			player.setXp(player.getXp() + points);
			
			// Level-up?
			int currentLevel = player.getLevel();
			int newLevel = Experience.calculateLevel(player.getXp());
			if(newLevel > currentLevel)
			{
				// Process level-ups
				for(int level = currentLevel + 1; level <= newLevel; level++)
				{
					onLevelUp(player, level);
				}
				
				// Save level
				player.setLevel(newLevel);
			}
		}
	}
	
	private void onLevelUp(Player player, int level)
	{
		// Hurray!
		logger.debug("DING, {} reached {}", player.getName(), level);
		
		// Yay, a coin!
		player.setBalance(player.getBalance() + 1);
		
		// Flush it down the activity stream
		LevelUpStreamItem lup = new LevelUpStreamItem();
		lup.setPlayer(player);
		lup.setLevel(level);
		super.getServer().getActivityStream().postItem(lup);
		
		// Publish it to Facebook?
		if(player.isFacebookLinked())
		{
			LevelReachedStory story = new LevelReachedStory(player.getFacebook(), level);
			super.getServer().getFacebook().publish(story);
		}
	}
	
	public void giveMatchExperience(MatchPlayer player, MatchPlayer winner)
	{
		// Not a bot?
		if(!(player instanceof MatchBotPlayer))
		{
			return;
		}
		
		// Base = score
		int xp = player.getScore();
		
		// Boost winner's XP
		if(player == winner)
		{
			xp *= 1.5;
		}
			
		// Add the experience and handle level ups
		this.addExperience(player.getInfo(), xp);
			
		// Notify session of experience gain
		Session session = player.getSession();
		session.send(new ExperienceAddedMessage(xp, player.getInfo().getXp()));
			
		// Save data
		session.saveData();
	}
}
