package strikd.game.match.bots;

import strikd.game.match.MatchPlayer;
import strikd.game.match.bots.impl.SimpleMatchBotPlayer;
import strikd.game.player.Avatars;
import strikd.game.player.Player;
import strikd.util.RandomUtil;

public class MatchBotFactory
{
	public MatchBotPlayer newBotForOpponent(MatchPlayer opponent)
	{
		Player bot = new Player();
		Player player = opponent.getInfo();
		
		// ID is negative & random
		bot.setId(-RandomUtil.pickInt(1, Integer.MAX_VALUE));
		
		// Name is random with emojis
		bot.setName(RandomUtil.flipCoin() ? MatchBotNameGenerator.generateMaleName() : MatchBotNameGenerator.generateFemaleName());
		
		// Avatar is random, too
		bot.setAvatar(Integer.toString(RandomUtil.pickInt(1, Avatars.AMOUNT_OF_AVATARS)));
		
		// Locale and country are equal to the player's
		bot.setLocale(player.getLocale());
		bot.setCountry(player.getCountry());
		
		// Statistics are similar to the player's
		bot.setMatches(RandomUtil.pickInt(1, player.getMatches()));
		bot.setWins(RandomUtil.pickInt(1, player.getWins()));
		bot.setXp(bot.getMatches() * 45);
		
		// Fabricate bot
		return new SimpleMatchBotPlayer(bot);
	}
}
