package strikd.game.board.tiles;

import strikd.game.board.triggers.Trigger;

public class TimedTile extends Tile
{
	public TimedTile(int x, int y, char letter, Trigger trigger)
	{
		super(x, y, letter, trigger);
	}
}
