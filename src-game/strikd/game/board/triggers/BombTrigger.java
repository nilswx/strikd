package strikd.game.board.triggers;

import strikd.game.board.Board;
import strikd.game.board.Direction8;
import strikd.game.board.Tile;
import strikd.game.match.MatchPlayer;

public class BombTrigger extends Trigger
{
	@Override
	public String getTypeName()
	{
		return "bomb";
	}

	@Override
	protected void onActivate(MatchPlayer player, Tile tile)
	{
		Board board = tile.getBoard();
		
		// Destroy all tiles directly around this one
		int destroyed = 0;
		for(Direction8 dir : Direction8.all())
		{
			Tile other = board.getTile(tile.getColumn() + dir.x, tile.getRow() + dir.y);
			if(other != null)
			{
				board.removeTile(other);
				destroyed++;
			}
		}
		
		// Anything destroyed?
		if(destroyed > 0)
		{
			board.update();
			player.getMatch().broadcast(board.generateUpdateMessage());
		}
	}
}
