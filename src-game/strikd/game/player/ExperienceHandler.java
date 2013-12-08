package strikd.game.player;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import strikd.Server;
import strikd.communication.outgoing.ExperienceAddedMessage;
import strikd.game.facebook.LevelReachedStory;
import strikd.game.facebook.PersonBeatedStory;
import strikd.game.match.Match;
import strikd.game.match.MatchPlayer;
import strikd.game.match.bots.MatchBotPlayer;
import strikd.game.stream.activity.FriendMatchResultStreamItem;
import strikd.game.stream.activity.LevelUpStreamItem;

public class ExperienceHandler extends Server.Referent
{
	private static final Logger logger = LoggerFactory.getLogger(ExperienceHandler.class);
	
	public ExperienceHandler(Server server)
	{
		super(server);
	}
	
	public int addExperience(Player player, int points)
	{
		// Can gain XP?
		if(points <= 0 || player.getLevel() >= Experience.MAX_LEVEL)
		{
			return 0;
		}
		else
		{
			// Determine new XP
			int newXP = player.getXp() + points;
			
			// Overflowing max experience?
			int maxXP = Experience.getLevelBegin(Experience.MAX_LEVEL);
			if(newXP > maxXP)
			{
				// Impose XP cap
				newXP = maxXP;
				
				// Adjust the amount of added points
				points = (maxXP - player.getXp());
			}
			
			// Set the new XP
			player.setXp(newXP);
			
			// Determine current and new level
			int currentLevel = player.getLevel();
			int newLevel = Experience.calculateLevel(newXP);
			
			// Leveling up?
			if(newLevel > currentLevel)
			{
				// Process level-ups
				for(int level = currentLevel + 1; level <= newLevel; level++)
				{
					this.onLevelUp(player, level);
				}
				
				// Save level
				player.setLevel(newLevel);
			}
		}
		
		return points;
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
		super.getServer().getActivityStream().write(lup);
		
		// Publish it to Facebook?
		if(player.isFacebookLinked())
		{
			LevelReachedStory story = new LevelReachedStory(player.getFacebook(), level);
			super.getServer().getFacebook().publish(story);
		}
	}
	
	private void giveMatchExperience(MatchPlayer p, MatchPlayer winner)
	{
		// Bots work for free
		if(p instanceof MatchBotPlayer)
		{
			return;
		}
		
		// Write statistics
		Player player = p.getInfo();
		player.setMatches(player.getMatches() + 1);
		
		// Not a draw?
		if(winner != null)
		{
			if(p == winner)
			{
				player.setWins(player.getWins() + 1);
			}
			else
			{
				player.setLosses(player.getLosses() + 1);
			}
		}
		
		// Base XP = score
		int xp = p.getScore();
		
		// Boost winner's XP based on victory
		if(p == winner)
		{
			MatchPlayer loser = winner.getOpponent();
			
			// Blabla difference etc
			xp += loser.getScore();
		}
		
		// Add the experience and handle level ups
		int added = this.addExperience(player, xp);
		if(added > 0)
		{
			p.getSession().send(new ExperienceAddedMessage(xp, player.getXp()));
		}
			
		// Save data
		p.getSession().saveData();
	}
	
	public void executePostMatchLogic(Match match, MatchPlayer p1, MatchPlayer p2, MatchPlayer winner)
	{
		// Reward with experience
		this.giveMatchExperience(p1, winner);
		this.giveMatchExperience(p2, winner);
		
		// Not a bot match?
		if(!(p1 instanceof MatchBotPlayer) && !(p2 instanceof MatchBotPlayer))
		{
			// Not a draw?
			if(winner != null)
			{
				// Match between friends?
				List<Integer> friendList = p1.getSession().getFriendList();
				if(friendList.contains(p2.getInfo().getId()))
				{
					// Determine loser
					MatchPlayer loser = winner.getOpponent();
					
					// Post result to stream
					FriendMatchResultStreamItem fmr = new FriendMatchResultStreamItem();
					fmr.setPlayer(winner.getInfo());
					fmr.setLoser(loser.getInfo());
					this.getServer().getActivityStream().write(fmr);
					
					// And to Facebook?
					if(winner.getInfo().isFacebookLinked() && loser.getInfo().isFacebookLinked())
					{
						PersonBeatedStory story = new PersonBeatedStory(winner.getInfo().getFacebook(), loser.getInfo().getFacebook().getUserId());
						this.getServer().getFacebook().publish(story);
					}
				}
			}
		}
	}
}
