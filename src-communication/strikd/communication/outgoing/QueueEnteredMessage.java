package strikd.communication.outgoing;

import strikd.communication.Opcodes;
import strikd.game.match.queues.PlayerQueue;
import strikd.net.codec.OutgoingMessage;

public class QueueEnteredMessage extends OutgoingMessage
{
	public QueueEnteredMessage(PlayerQueue queue)
	{
		super(Opcodes.Outgoing.QUEUE_ENTERED);
		super.writeStr(queue.getName());
		super.writeInt(queue.getAvgWaitingTime());
	}
}
