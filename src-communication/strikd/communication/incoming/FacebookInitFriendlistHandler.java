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
		// User has Facebook and not initialized yet?
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
			
			// Retrieve mapping (player -> user)
			Map<Integer, Long> mapping = session.getServer().getPlayerRegister().getFacebookMapping(userIds);
			
			// Store the friendlist in the session
			session.setFriendList(ImmutableList.copyOf(mapping.keySet()));
			
			// Follow ALL friends in the stream (prevent duplicates when reinitializing)
			if(session.getFollowing().size() > 1)
			{
				session.getFollowing().clear();
				session.getFollowing().add(player.getId());
			}
			session.getFollowing().addAll(session.getFriendList());
			
			// Send the mapping!
			session.send(new FacebookFriendPlayersMessage(mapping));
		}
	}
}
