package strikd.communication.outgoing;

import strikd.communication.Opcodes;
import strikd.game.match.MatchPlayer;
import strikd.net.codec.OutgoingMessage;

public class TileSelectionClearedMessage extends OutgoingMessage
{
	public TileSelectionClearedMessage(MatchPlayer player)
	{
		super(Opcodes.Outgoing.TILE_SELECTION_CLEARED);
		super.writeByte((byte)player.getPlayerId());
	}
}
