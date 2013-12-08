package strikd.game.match.bots.impl;

import strikd.game.match.bots.MatchBotPlayer;
import strikd.game.player.Player;

public final class IdleMatchBotPlayer extends MatchBotPlayer
{
	public IdleMatchBotPlayer(Player bot)
	{
		super(bot);
	}
	
	@Override
	protected boolean initializeAI()
	{
		return false;
	}

	@Override
	protected int nextMoveDelay()
	{
		throw new UnsupportedOperationException("idle bot");
	}

	@Override
	protected void nextMove()
	{
		throw new UnsupportedOperationException("idle bot");
	}
}
