package strikd.communication.outgoing;

import strikd.communication.Opcodes;
import strikd.game.user.Avatar;
import strikd.net.codec.OutgoingMessage;

public class AvatarMessage extends OutgoingMessage
{
	public AvatarMessage(Avatar avatar)
	{
		super(Opcodes.Outgoing.AVATAR);
		super.writeStr(avatar != null ? avatar.toString() : "");
	}
}
