package strikd.game.match;

import strikd.game.user.User;
import strikd.net.codec.OutgoingMessage;

public class MatchBotPlayer extends MatchPlayer
{
	private User bot;
	
	public MatchBotPlayer(User bot)
	{
		super(null);
		this.bot = bot;
		this.setReady();
	}
	
	@Override
	public void send(OutgoingMessage msg)
	{
		// /dev/null
	}
	
	@Override
	public User getInfo()
	{
		return this.bot;
	}
}
