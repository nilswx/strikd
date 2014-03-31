package strikd.communication.outgoing;

import strikd.communication.Opcodes;
import strikd.game.player.Player;
import strikd.net.codec.OutgoingMessage;

public class ChallengeRevokedMessage extends OutgoingMessage
{
	public ChallengeRevokedMessage(Player player)
	{
		super(Opcodes.Outgoing.CHALLENGE_REVOKED);
		super.writeInt(player.getId());
	}
}
