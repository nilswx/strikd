package strikd.communication.outgoing;

import strikd.communication.Opcodes;
import strikd.game.board.Board;
import strikd.game.board.Tile;
import strikd.net.codec.OutgoingMessage;

public class BoardInitMessage extends OutgoingMessage
{
	public BoardInitMessage(Board board)
	{
		super(Opcodes.Outgoing.BOARD_INIT);
		
		// Dimensions
		int width = board.getWidth(), height = board.getHeight();
		super.writeByte((byte)width);
		super.writeByte((byte)height);
		
		// All tiles
		for(int column = 0; column < width; column++)
		{
			for(int row = 0; row < height; row++)
			{
				// Something here?
				Tile tile = board.getTile(column, row);
				if(tile == null)
				{
					// Nothing 'ere!
					super.writeByte((byte)0);
				}
				else
				{
					super.writeByte(tile.getTileId());
					// TODO: store trigger info in remaining 3 bits (square.getLetter() << 5))
					super.writeByte((byte)tile.getLetter());
				}
			}
		}
	}
}
