package strikd.communication.outgoing;

import java.util.List;

import strikd.communication.Opcodes;
import strikd.game.player.Player;
import strikd.net.codec.OutgoingMessage;

public class FacebookFriendsUpdateMessage extends OutgoingMessage
{
	public FacebookFriendsUpdateMessage(List<Player> friends)
	{
		super(Opcodes.Outgoing.FACEBOOK_FRIENDS_UPDATE);
		super.writeInt(friends.size());
		for(Player friend : friends)
		{
			super.writeInt(friend.getId());
			super.writeStr(friend.getName());
			super.writeStr(friend.getAvatar());
			super.writeBool(friend.isOnline());
			super.writeBool(friend.isInMatch());
		}
	}
}
