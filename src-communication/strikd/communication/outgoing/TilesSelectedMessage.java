package strikd.communication.outgoing;

import java.util.List;

import strikd.communication.Opcodes;
import strikd.game.board.Square;
import strikd.net.codec.OutgoingMessage;

public class TilesSelectedMessage extends OutgoingMessage
{
	public TilesSelectedMessage(int playerId, List<Square> squares)
	{
		super(Opcodes.Outgoing.CREDENTIALS);
		super.writeByte((byte)playerId);
		super.writeByte((byte)squares.size());
		for(Square square : squares)
		{
			super.writeByte((byte)(square.x << 4 | square.y));
		}
	}
}
