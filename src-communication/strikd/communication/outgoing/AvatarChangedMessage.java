package strikd.communication.outgoing;

import strikd.communication.Opcodes;
import strikd.net.codec.OutgoingMessage;

public class AvatarChangedMessage extends OutgoingMessage
{
	public AvatarChangedMessage(String newAvatar)
	{
		super(Opcodes.Outgoing.AVATAR_CHANGED);
		super.writeStr(newAvatar);
	}
}
