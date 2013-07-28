package strikd.game.match;

import strikd.game.player.Player;

public class MatchBotPlayer extends MatchPlayer
{
	private Player bot;
	
	public MatchBotPlayer(Player bot)
	{
		super(null);
	}

	@Override
	public Player getPlayer()
	{
		return this.bot;
	}
	
	public void performAction()
	{
		
	}
}
