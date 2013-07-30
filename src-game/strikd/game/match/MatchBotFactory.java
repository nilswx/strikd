package strikd.game.match;

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
		bot.name = "bot" + rand.nextInt(1000);
		bot.avatar = new Avatar();
		
		return new MatchBotPlayer(bot);
	}
}
