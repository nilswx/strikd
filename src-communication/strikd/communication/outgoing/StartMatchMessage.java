package strikd.communication.outgoing;

import strikd.communication.Opcodes;
import strikd.net.codec.OutgoingMessage;

public class StartMatchMessage extends OutgoingMessage
{
	public StartMatchMessage()
	{
		super(Opcodes.Outgoing.START_MATCH);
	}
}
