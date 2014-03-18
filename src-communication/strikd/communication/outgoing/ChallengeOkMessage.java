package strikd.communication.outgoing;

import strikd.communication.Opcodes;
import strikd.game.player.Player;
import strikd.net.codec.OutgoingMessage;

public class ChallengeOkMessage extends OutgoingMessage
{
	public ChallengeOkMessage(Player player)
	{
		super(Opcodes.Outgoing.CHALLENGE_OK);
		super.writeInt(player.getId());
	}
}
