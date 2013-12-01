package strikd.communication.outgoing;

import java.util.Map;
import java.util.Map.Entry;

import strikd.communication.Opcodes;
import strikd.net.codec.OutgoingMessage;

public class FacebookFriendPlayersMessage extends OutgoingMessage
{
	public FacebookFriendPlayersMessage(Map<Integer, Long> mapping)
	{
		super(Opcodes.Outgoing.FACEBOOK_FRIEND_PLAYERS);
		super.writeInt(mapping.size());
		for(Entry<Integer, Long> entry : mapping.entrySet())
		{
			super.writeInt(entry.getKey());
			super.writeLong(entry.getValue());
		}
	}
}
