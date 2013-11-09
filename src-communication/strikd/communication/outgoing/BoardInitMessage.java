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
		int width = board.getWidth(), height = board.getHeight();
		super.writeByte((byte)width);
		super.writeByte((byte)height);
		
		// All tiles
		for(int x = 0; x < width; x++)
		{
			for(int y = 0; y < height; y++)
			{
				Square square = board.getSquare(x, y);
				if(square == null)
				{
					super.writeByte((byte)0);
				}
				else
				{
					// TODO: store trigger info in remaining 3 bits (square.getLetter() << 5))
					super.writeByte((byte)square.getLetter());
				}
			}
		}
	}
}
