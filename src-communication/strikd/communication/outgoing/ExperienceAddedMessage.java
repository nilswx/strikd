package strikd.communication.outgoing;

import strikd.communication.Opcodes;
import strikd.net.codec.OutgoingMessage;

public class ExperienceAddedMessage extends OutgoingMessage
{
	public ExperienceAddedMessage(int added, int newTotal)
	{
		super(Opcodes.Outgoing.EXPERIENCE_ADDED);
		super.writeInt(added);
		super.writeInt(newTotal);
	}
}
