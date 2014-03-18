package strikd.communication.outgoing;

import strikd.cluster.ServerDescriptor;
import strikd.communication.Opcodes;
import strikd.game.player.Player;
import strikd.net.codec.OutgoingMessage;

public class ChallengeRedirectMessage extends OutgoingMessage
{
	public ChallengeRedirectMessage(Player player, ServerDescriptor server)
	{
		super(Opcodes.Outgoing.CHALLENGE_REDIRECT);
		super.writeInt(player.getId());
		super.writeStr(server.getHost());
		super.writeStr(server.getPort());
	}
}
