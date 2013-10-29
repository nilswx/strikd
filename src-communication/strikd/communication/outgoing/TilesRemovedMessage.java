package strikd.communication.outgoing;

import java.util.List;

import strikd.communication.Opcodes;
import strikd.game.board.Square;
import strikd.net.codec.OutgoingMessage;

public class TilesRemovedMessage extends OutgoingMessage
{
	public TilesRemovedMessage(List<Square> tiles)
	{
		super(Opcodes.Outgoing.TILES_ADDED);
		super.writeByte((byte)tiles.size());
		for(Square tile : tiles)
		{
			super.writeByte((byte)(tile.x << 4 | tile.y));
		}
	}
}
