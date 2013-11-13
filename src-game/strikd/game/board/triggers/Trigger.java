package strikd.game.board.triggers;

import strikd.game.match.MatchPlayer;

public abstract class Trigger
{
	public abstract String getTypeName();

	public abstract void execute(MatchPlayer player);
}
