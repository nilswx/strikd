package strikd.communication.outgoing;

import java.util.Map;
import java.util.Map.Entry;

import strikd.communication.Opcodes;
import strikd.net.codec.OutgoingMessage;

public class FacebookFriendPlayersMessage extends OutgoingMessage
{
	public FacebookFriendPlayersMessage(Map<Long, Long> mapping)
	{
		super(Opcodes.Outgoing.FACEBOOK_FRIEND_PLAYERS);
		super.writeInt(mapping.size());
		for(Entry<Long, Long> entry : mapping.entrySet())
		{
			super.writeLong(entry.getKey());
			super.writeLong(entry.getValue());
		}
	}
}
