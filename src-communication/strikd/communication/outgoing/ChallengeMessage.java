package strikd.communication.outgoing;

import strikd.communication.Opcodes;
import strikd.net.codec.OutgoingMessage;

public class ChallengeMessage extends OutgoingMessage
{
	public ChallengeMessage(int playerId)
	{
		super(Opcodes.Outgoing.CHALLENGE);
		super.writeInt(playerId);
	}
}
