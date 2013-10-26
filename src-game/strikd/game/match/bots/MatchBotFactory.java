package strikd.game.match.bots;

import java.util.Random;

import org.bson.types.ObjectId;

import strikd.game.user.Avatar;
import strikd.game.user.User;

public class MatchBotFactory
{
	public MatchBotPlayer newBot()
	{
		Random rand = new Random();
		
		User bot = new User();
		bot.id = ObjectId.get();
		bot.name = String.format("Bot-%d", rand.nextInt(1000));
		bot.avatar = new Avatar();
		bot.country = "bot"; // A cog wheel or so!
		
		bot.matches = rand.nextInt(300) + 1;
		bot.wins = rand.nextInt(bot.matches);
		bot.xp = bot.matches * 45;
		
		return new MatchBotPlayer(bot);
	}
}
