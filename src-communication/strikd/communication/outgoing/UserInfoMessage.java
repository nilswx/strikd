package strikd.communication.outgoing;

import strikd.communication.Opcodes;
import strikd.game.user.User;
import strikd.net.codec.OutgoingMessage;

public class UserInfoMessage extends OutgoingMessage
{
	public UserInfoMessage(User u)
	{
		super(Opcodes.Outgoing.USER_INFO);
		super.writeStr(u.id.toString());
		super.writeStr(u.name);
		super.writeStr(u.avatar != null ? u.avatar.toString() : "");
	}
}
