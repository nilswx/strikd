package strikd.game.match.bots;

import java.util.Random;

import strikd.game.match.MatchPlayer;
import strikd.game.match.bots.impl.SimpleMatchBotPlayer;
import strikd.game.player.Player;

public class MatchBotFactory
{
	public MatchBotPlayer newBotForOpponent(MatchPlayer opponent)
	{
		Random rand = new Random();
		
		Player bot = new Player();
		bot.setId(488228);
		bot.setName(String.format("Bot-%d", rand.nextInt(1000)));
		bot.saveAvatar();
		bot.setLocale(opponent.getInfo().getLocale());
		bot.setCountry("de"); // FROM DEUTSCHLAND!
		
		bot.setMatches(rand.nextInt(300) + 1);
		bot.setWins(rand.nextInt(bot.getMatches()));
		bot.setXp(bot.getMatches() * 45);
		
		return new SimpleMatchBotPlayer(bot);
	}
}
