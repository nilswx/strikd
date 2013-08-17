package strikd.communication.outgoing;

import strikd.communication.Opcodes;
import strikd.net.codec.OutgoingMessage;

public class QueueExitedMessage extends OutgoingMessage
{
	public QueueExitedMessage()
	{
		super(Opcodes.Outgoing.QUEUE_EXITED);
	}
}
