package strikd.communication.outgoing;

import strikd.communication.Opcodes;
import strikd.game.board.Board;
import strikd.game.board.Square;
import strikd.net.codec.OutgoingMessage;

public class BoardInitMessage extends OutgoingMessage
{
	public BoardInitMessage(Board board)
	{
		super(Opcodes.Outgoing.BOARD_INIT);
		
		// Dimensions
		super.writeByte((byte)board.getWidth());
		super.writeByte((byte)board.getHeight());
		
		// All tiles
		for(int x = 0; x < board.getWidth(); x++)
		{
			for(int y = 0; y < board.getHeight(); y++)
			{
				Square square = board.getSquareUnchecked(x, y);
				if(square.isNull())
				{
					super.writeByte((byte)0);
				}
				else
				{
					super.writeByte((byte) (square.getLetter() << 5)); // TODO: store trigger info in remaining 3 bits
				}
			}
		}
	}
}
