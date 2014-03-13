package strikd.communication.outgoing;

import strikd.communication.Opcodes;
import strikd.net.codec.OutgoingMessage;

public class ChallengeDeclinedMessage extends OutgoingMessage
{
	public ChallengeDeclinedMessage(int playerId)
	{
		super(Opcodes.Outgoing.CHALLENGE_DECLINED);
		super.writeInt(playerId);
	}
}
