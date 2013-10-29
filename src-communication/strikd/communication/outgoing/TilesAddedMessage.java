package strikd.communication.outgoing;

import java.util.List;

import strikd.communication.Opcodes;
import strikd.game.board.Square;
import strikd.net.codec.OutgoingMessage;

public class TilesAddedMessage extends OutgoingMessage
{
	public TilesAddedMessage(List<Square> tiles)
	{
		super(Opcodes.Outgoing.TILES_ADDED);
		super.writeByte((byte)tiles.size());
		for(Square tile : tiles)
		{
			super.writeByte((byte)tile.x);
			if(tile.isNull())
			{
				super.writeByte((byte)0);
			}
			else
			{
				super.writeByte((byte) (tile.getLetter() << 5)); // TODO: store trigger info in remaining 3 bits
			}
		}
	}
}
