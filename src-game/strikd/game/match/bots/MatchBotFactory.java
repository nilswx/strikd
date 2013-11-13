package strikd.game.match.bots;

import java.util.Random;

import org.bson.types.ObjectId;

import strikd.game.match.MatchPlayer;
import strikd.game.player.Avatar;
import strikd.game.player.Player;

public class MatchBotFactory
{
	public MatchBotPlayer newBotForOpponent(MatchPlayer opponent)
	{
		Random rand = new Random();
		
		Player bot = new Player();
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
