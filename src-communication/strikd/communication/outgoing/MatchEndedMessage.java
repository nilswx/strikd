package strikd.communication.outgoing;

import strikd.communication.Opcodes;
import strikd.net.codec.OutgoingMessage;

public class MatchEndedMessage extends OutgoingMessage
{
	public MatchEndedMessage()
	{
		super(Opcodes.Outgoing.MATCH_ENDED);
	}
}
