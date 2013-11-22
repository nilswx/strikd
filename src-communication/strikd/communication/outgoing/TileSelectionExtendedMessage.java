package strikd.communication.outgoing;

import java.util.List;

import strikd.communication.Opcodes;
import strikd.game.board.Tile;
import strikd.game.match.MatchPlayer;
import strikd.net.codec.OutgoingMessage;

public class TileSelectionExtendedMessage extends OutgoingMessage
{
	public TileSelectionExtendedMessage(MatchPlayer player, List<Tile> tiles)
	{
		super(Opcodes.Outgoing.TILE_SELECTION_EXTENDED);
		super.writeByte((byte)player.getPlayerId());
		super.writeByte((byte)tiles.size());
		for(Tile tile : tiles)
		{
			super.writeByte(tile.getTileId());
		}
	}
}
