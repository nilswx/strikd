package strikd.communication.outgoing;

import java.util.List;

import strikd.communication.Opcodes;
import strikd.game.board.Square;
import strikd.net.codec.OutgoingMessage;

public class TilesSelectedMessage extends OutgoingMessage
{
	public TilesSelectedMessage(int playerId, List<Square> tiles)
	{
		super(Opcodes.Outgoing.TILES_SELECTED);
		super.writeByte((byte)playerId);
		super.writeByte((byte)tiles.size());
		for(Square tile : tiles)
		{
			super.writeByte((byte)(tile.x << 4 | tile.y));
		}
	}
}
