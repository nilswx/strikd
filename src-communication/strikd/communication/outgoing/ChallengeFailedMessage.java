package strikd.communication.outgoing;

import strikd.communication.Opcodes;
import strikd.game.player.Player;
import strikd.net.codec.OutgoingMessage;

public class ChallengeFailedMessage extends OutgoingMessage
{
	public ChallengeFailedMessage(Player opponent)
	{
		this(opponent.getId());
	}
	
	public ChallengeFailedMessage(int playerId)
	{
		super(Opcodes.Outgoing.CHALLENGE_FAILED);
		super.writeInt(playerId);
	}
}
