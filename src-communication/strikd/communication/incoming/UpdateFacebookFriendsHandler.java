package strikd.communication.incoming;

import java.util.List;

import strikd.sessions.Session;
import strikd.communication.Opcodes;
import strikd.communication.outgoing.FacebookFriendsMessage;
import strikd.game.player.Player;
import strikd.net.codec.IncomingMessage;

public class UpdateFacebookFriendsHandler extends MessageHandler
{
	@Override
	public Opcodes.Incoming getOpcode()
	{
		return Opcodes.Incoming.GET_FACEBOOK_FRIENDS;
	}
	
	@Override
	public void handle(Session session, IncomingMessage request)
	{
		// User has Facebook?
		Player player = session.getPlayer();
		if(player.isFacebookLinked())
		{
			// Collect all friend ID's
			long[] friendIds = new long[request.readInt()];
			for(int i = 0; i < friendIds.length; i++)
			{
				friendIds[i] = request.readLong();
			}
			
			// Send the player the latest friendlist
			List<Player> friends = session.getServer().getPlayerRegister().getFriendPlayers(friendIds);
			session.send(new FacebookFriendsMessage(friends));
		}
	}
}
