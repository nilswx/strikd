package strikd.communication.outgoing;

import strikd.communication.Opcodes;
import strikd.game.player.Player;
import strikd.net.codec.OutgoingMessage;

public class ChallengeMessage extends OutgoingMessage
{
	public ChallengeMessage(Player player)
	{
		super(Opcodes.Outgoing.CHALLENGE);
		super.writeInt(player.getId());
	}
}
