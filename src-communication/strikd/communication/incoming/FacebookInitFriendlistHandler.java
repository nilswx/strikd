package strikd.communication.incoming;

import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import strikd.sessions.Session;
import strikd.communication.Opcodes;
import strikd.communication.outgoing.FacebookFriendPlayersMessage;
import strikd.game.player.Player;
import strikd.net.codec.IncomingMessage;

public class FacebookInitFriendlistHandler extends MessageHandler
{
	@Override
	public Opcodes.Incoming getOpcode()
	{
		return Opcodes.Incoming.FACEBOOK_INIT_FRIENDLIST;
	}
	
	@Override
	public void handle(Session session, IncomingMessage request)
	{
		// User has Facebook?
		Player player = session.getPlayer();
		if(player.isFacebookLinked())
		{
			// Collect all friend ID's (Facebook user ID)
			int amount = request.readInt();
			List<Long> userIds = Lists.newArrayListWithCapacity(amount);
			for(int i = 0; i < amount; i++)
			{
				userIds.add(request.readLong());
			}
			
			// Retrieve mapping (user -> player)
			Map<Long, Long> mapping = session.getServer().getPlayerRegister().getFacebookMapping(userIds);
			
			// Store the player IDs in session
			session.setFriendList(ImmutableList.copyOf(mapping.values()));
			
			// Send the mapping!
			session.send(new FacebookFriendPlayersMessage(mapping));
		}
	}
}
