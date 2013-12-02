package strikd.communication.outgoing;

import strikd.communication.Opcodes;
import strikd.game.player.Player;
import strikd.net.codec.OutgoingMessage;

public class FacebookRecoverResultMessage extends OutgoingMessage
{
	public FacebookRecoverResultMessage(Player player)
	{
		super(Opcodes.Outgoing.FACEBOOK_RECOVER_RESULT);
		super.writeLong(player == null ? 0 : player.getId());
		super.writeStr(player == null ? null : player.getToken());
	}
}
