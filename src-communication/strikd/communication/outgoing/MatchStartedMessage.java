package strikd.communication.outgoing;

import strikd.communication.Opcodes;
import strikd.net.codec.OutgoingMessage;

public class MatchStartedMessage extends OutgoingMessage
{
	public MatchStartedMessage()
	{
		super(Opcodes.Outgoing.MATCH_STARTED);
	}
}
