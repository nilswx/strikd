package strikd.communication.outgoing;

import strikd.communication.Opcodes;
import strikd.net.codec.OutgoingMessage;

public class QueueEnteredMessage extends OutgoingMessage
{
	public QueueEnteredMessage(String name, int avgWaitingTime)
	{
		super(Opcodes.Outgoing.QUEUE_ENTERED);
		super.writeStr(name);
		super.writeInt(avgWaitingTime);
	}
}
