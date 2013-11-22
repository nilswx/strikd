package strikd.communication.outgoing;

import java.util.List;

import strikd.communication.Opcodes;
import strikd.game.player.FriendPlayer;
import strikd.net.codec.OutgoingMessage;

public class FacebookFriendsMessage extends OutgoingMessage
{
	public FacebookFriendsMessage(List<FriendPlayer> friends)
	{
		super(Opcodes.Outgoing.FACEBOOK_FRIENDS);
		super.writeInt(friends.size());
		for(FriendPlayer friend : friends)
		{
			super.writeLong(friend.getId());
			super.writeStr(friend.getName());
			super.writeStr(friend.getAvatar());
			super.writeLong(friend.getFacebook().getUserId());
			super.writeStr(friend.getFacebook().getName());
			super.writeBool(friend.isOnline());
			super.writeBool(friend.isInMatch());
		}
	}
}
