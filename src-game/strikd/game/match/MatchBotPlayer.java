package strikd.game.match;

import strikd.game.user.User;

public class MatchBotPlayer extends MatchPlayer
{
	private User bot;
	
	public MatchBotPlayer(User bot)
	{
		super(null);
		this.bot = bot;
	}

	@Override
	public User getInfo()
	{
		return this.bot;
	}
	
	public void performAction()
	{
		
	}
}
