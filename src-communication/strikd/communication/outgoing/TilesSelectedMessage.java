package strikd.communication.outgoing;

import java.util.Collections;
import java.util.List;

import strikd.communication.Opcodes;
import strikd.game.board.Square;
import strikd.game.match.MatchPlayer;
import strikd.net.codec.OutgoingMessage;

public class TilesSelectedMessage extends OutgoingMessage
{
	public TilesSelectedMessage(MatchPlayer player, List<Square> tiles)
	{
		super(Opcodes.Outgoing.TILES_SELECTED);
		super.writeByte((byte)player.getPlayerId());
		super.writeByte((byte)tiles.size());
		for(Square tile : tiles)
		{
			super.writeByte((byte)(tile.x << 4 | tile.y));
		}
	}
	
	public static final List<Square> CLEAR_SELECTION = Collections.emptyList();
}
