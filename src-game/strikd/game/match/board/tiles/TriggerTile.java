package strikd.game.match.board.tiles;

import strikd.game.match.board.triggers.Trigger;

public class TriggerTile extends Tile
{
	private final Trigger trigger;
	
	public TriggerTile(int x, int y, char letter, Trigger trigger)
	{
		super(x, y, letter);
		this.trigger = trigger;
	}
	
	public Trigger getTrigger()
	{
		return this.trigger;
	}
}
