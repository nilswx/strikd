package strikd.communication.outgoing;

import java.util.List;

import strikd.communication.Opcodes;
import strikd.game.board.Square;
import strikd.game.match.MatchPlayer;
import strikd.net.codec.OutgoingMessage;

public class TileSelectionExtendedMessage extends OutgoingMessage
{
	public TileSelectionExtendedMessage(MatchPlayer player, List<Square> tiles)
	{
		super(Opcodes.Outgoing.TILE_SELECTION_EXTENDED);
		super.writeByte((byte)player.getPlayerId());
		super.writeByte((byte)tiles.size());
		for(Square tile : tiles)
		{
			super.writeByte((byte)(tile.getColumn() << 4 | tile.getRow()));
		}
	}
}
