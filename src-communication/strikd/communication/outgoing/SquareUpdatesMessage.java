package strikd.communication.outgoing;

import strikd.communication.Opcodes;
import strikd.game.board.BoardUpdateGenerator;
import strikd.game.board.tiles.Square;
import strikd.net.codec.OutgoingMessage;

public class SquareUpdatesMessage extends OutgoingMessage
{
	// payload size: 1 + (tiles * 3)

	public SquareUpdatesMessage(BoardUpdateGenerator collector)
	{
		super(Opcodes.Outgoing.SQUARE_UPDATES);
	}
	
	public void appendCount(int count)
	{
		super.writeByte((byte)count);
	}
	
	public void appendUpdate(Square square)
	{
		super.writeByte((byte)(square.x << 4 | square.y));
		if(square.isNull())
		{
			super.writeByte((byte)0);
		}
		else
		{
			super.writeByte((byte)square.getLetter());
		}
		
		/*
		super.writeByte((byte) (square.x << 4 | square.y));
		if(square.isNull())
		{
			super.writeByte((byte)0);
		}
		else
		{
			super.writeByte((byte) (square.getLetter() << 5)); // TODO: store trigger info in remaining 3 bits
		}*/
	}
}
