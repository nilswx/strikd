package strikd.communication.incoming;

import java.util.ArrayList;
import java.util.List;

import strikd.communication.Opcodes;
import strikd.game.board.Board;
import strikd.game.board.Tile;
import strikd.game.match.Match;
import strikd.game.match.MatchPlayer;
import strikd.game.match.SelectionValidator;
import strikd.net.codec.IncomingMessage;
import strikd.sessions.Session;

public class SelectTilesHandler extends MessageHandler
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
			List<Tile> tiles = new ArrayList<Tile>();
			for(int i = 0; i < amount; i++)
			{
                Tile tile = board.getTile(request.readByte());
				if(tile != null)
				{
                    tiles.add(tile);
				}
			}
			
			// Validate selection
			SelectionValidator.validateSelection(player, tiles);
		}
	}
}
