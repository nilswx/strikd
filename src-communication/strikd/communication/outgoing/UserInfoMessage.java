package strikd.communication.outgoing;

import strikd.communication.Opcodes;
import strikd.game.user.User;
import strikd.net.codec.OutgoingMessage;

public class UserInfoMessage extends OutgoingMessage
{
	public UserInfoMessage(User user)
	{
		super(Opcodes.Outgoing.USER_INFO);
		serializeUser(user, this);
	}
	
	public static void serializeUser(User user, OutgoingMessage msg)
	{
		msg.writeStr(user.id.toString());
		msg.writeStr(user.name);
		msg.writeStr(user.avatar != null ? user.avatar.toString() : "");
		msg.writeStr(user.country);
		msg.writeInt(user.xp);
		msg.writeInt(user.matches);
		msg.writeInt(user.wins);
	}
}
