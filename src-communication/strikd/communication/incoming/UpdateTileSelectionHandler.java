package strikd.communication.incoming;

import java.util.ArrayList;
import java.util.List;

import strikd.communication.Opcodes;
import strikd.communication.outgoing.TileSelectionExtendedMessage;
import strikd.game.board.Board;
import strikd.game.board.Tile;
import strikd.game.match.Match;
import strikd.game.match.MatchPlayer;
import strikd.game.match.SelectionValidator;
import strikd.net.codec.IncomingMessage;
import strikd.sessions.Session;

public class UpdateTileSelectionHandler extends MessageHandler
{
	@Override
	public Opcodes.Incoming getOpcode()
	{
		return Opcodes.Incoming.UPDATE_TILE_SELECTION;
	}
	
	@Override
	public void handle(Session session, IncomingMessage request)
	{
		// In valid match, and not frozen, etc?
		if(session.isInMatch())
		{
			// Short-hand variables
			MatchPlayer player = session.getMatchPlayer();
			Match match = player.getMatch();
			Board board = match.getBoard();
			
			// Select all specified tiles
			int amount = request.readByte();
			List<Tile> newSelected = new ArrayList<Tile>();
			for(int i = 0; i < amount; i++)
			{
                Tile tile = board.getTile(request.readByte());
				if(tile != null)
				{
                    player.selectTile(tile);
                    newSelected.add(tile);
				}
			}
			
			// Was this the last batch?
			boolean isComplete = request.readBool();
			if(!isComplete)
			{
				// Any updates?
				if(newSelected.size() > 0)
				{
					// Just to opponent
					player.getOpponent().send(new TileSelectionExtendedMessage(player, newSelected));
				}
			}
			else
			{
				SelectionValidator.validateSelection(player);
			}
		}
	}
}
