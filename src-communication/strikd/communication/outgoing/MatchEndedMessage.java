package strikd.communication.outgoing;

import strikd.communication.Opcodes;
import strikd.game.match.MatchPlayer;
import strikd.net.codec.OutgoingMessage;

public class MatchEndedMessage extends OutgoingMessage
{
	public MatchEndedMessage(MatchPlayer winner)
	{
		super(Opcodes.Outgoing.MATCH_ENDED);
		super.writeByte((byte)(winner != null ? winner.getPlayerId() : -1));
	}
}
