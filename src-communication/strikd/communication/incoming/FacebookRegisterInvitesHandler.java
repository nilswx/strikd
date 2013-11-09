package strikd.communication.incoming;

import strikd.sessions.Session;
import strikd.communication.Opcodes;
import strikd.game.facebook.FacebookInviteManager;
import strikd.game.player.Player;
import strikd.net.codec.IncomingMessage;

public class FacebookRegisterInvitesHandler extends MessageHandler
{
	@Override
	public Opcodes.Incoming getOpcode()
	{
		return Opcodes.Incoming.FACEBOOK_REGISTER_INVITES;
	}
	
	@Override
	public void handle(Session session, IncomingMessage request)
	{
		// Allowed to invite users?
		Player player = session.getPlayer();
		if(player.isFacebookLinked())
		{
			// Get invite manager
			FacebookInviteManager inviteMgr = session.getServer().getFacebook().getInviteMgr();
			
			// Register all invites with the reward system
			int amount = request.readInt();
			for(int i = 0; i < amount; i++)
			{
				inviteMgr.registerInvite(request.readLong(), player);
			}
		}
	}
}
