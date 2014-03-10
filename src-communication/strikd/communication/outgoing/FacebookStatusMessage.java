package strikd.communication.outgoing;

import strikd.communication.Opcodes;
import strikd.game.player.Player;
import strikd.net.codec.OutgoingMessage;

public class FacebookStatusMessage extends OutgoingMessage
{
	public FacebookStatusMessage(Player player)
	{
		super(Opcodes.Outgoing.FACEBOOK_STATUS);
		super.writeLong(player.isFacebookLinked() ? player.getFacebook().getUserId() : 0);
		super.writeBool(player.isLiked());
	}
}
