package strikd.game.match.bots.impl;

import java.util.List;

import com.google.common.collect.Lists;

import strikd.game.match.MatchPlayer;
import strikd.game.match.bots.MatchBotPlayer;
import strikd.game.player.Player;
import strikd.util.RandomUtil;

public final class TutorialMatchBotPlayer extends MatchBotPlayer
{
	private final List<String> messages = Lists.newArrayList();
	
	public TutorialMatchBotPlayer(Player bot)
	{
		super(bot);
		
		bot.setName("Tuti");
		bot.setCountry("uk");
	}
	
	@Override
	protected boolean initializeAI()
	{
		this.messages.add("Hey %s, I'm an awesome tutorial bot!");
		this.messages.add("Oh %s, where would you be without me!");
		this.messages.add("I should be giving you some fairly useful hints now, hm?");
		this.messages.add("God save the queen, save our gracious queen...");
		
		return true;
	}

	@Override
	protected int nextMoveDelay()
	{
		return RandomUtil.pickInt(5*1000, 10*1000);
	}

	@Override
	protected void nextMove()
	{
		MatchPlayer opponent = this.getOpponent();
		if(opponent.getSession() != null)
		{
			String msg = RandomUtil.pickOne(this.messages);
			opponent.getSession().sendAlert(msg, opponent.getInfo().getName());
		}
	}
}
