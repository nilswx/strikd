package strikd.communication.incoming;

import strikd.sessions.Session;
import strikd.communication.Opcodes;
import strikd.game.board.Board;
import strikd.game.board.Square;
import strikd.net.codec.IncomingMessage;

public class SelectTilesHandler extends MessageHandler
{
	@Override
	public Opcodes.Incoming getOpcode()
	{
		return Opcodes.Incoming.SELECT_TILES;
	}
	
	@Override
	public void handle(Session session, IncomingMessage request)
	{
		// In valid match, and not frozen, etc?
		if(session.isInMatch())
		{
			Board board = session.getMatch().getBoard();
			
			// Select all specified tiles
			int amount = request.readByte();
			for(int i = 0; i < amount; i++)
			{
				// Unpack position
				byte pos = request.readByte();
				int x = pos >> 4;
				int y = pos & 0x0F;
						
				// In range?
				if(board.squareExists(x, y))
				{
					// Is actually a tile?
					Square square = board.getSquare(x, y);
					if(square.isTile())
					{
						System.out.println(String.format("%s selected %s", session.getUser(), square));
					}
				}
			}
		}
	}
}
