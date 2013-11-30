package strikd.communication.incoming;

import java.util.List;

import strikd.sessions.Session;
import strikd.communication.Opcodes;
import strikd.communication.outgoing.FacebookFriendsUpdateMessage;
import strikd.game.player.Player;
import strikd.net.codec.IncomingMessage;

public class FacebookRefreshFriendsHandler extends MessageHandler
{
	@Override
	public Opcodes.Incoming getOpcode()
	{
		return Opcodes.Incoming.FACEBOOK_INIT_FRIENDLIST;
	}
	
	@Override
	public void handle(Session session, IncomingMessage request)
	{
		// Friendlist initialized?
		List<Long> friendList = session.getFriendList();
		if(friendList != null)
		{
			List<Player> players = session.getServer().getPlayerRegister().getPlayers(friendList);
			session.send(new FacebookFriendsUpdateMessage(players));
		}
	}
}
