package strikd.communication.incoming;

import strikd.sessions.Session;
import strikd.communication.Opcodes;
import strikd.communication.outgoing.FacebookStatusMessage;
import strikd.game.player.Player;
import strikd.net.codec.IncomingMessage;

public class FacebookUnlinkHandler extends MessageHandler
{
	@Override
	public Opcodes.Incoming getOpcode()
	{
		return Opcodes.Incoming.FACEBOOK_UNLINK;
	}
	
	@Override
	public void handle(Session session, IncomingMessage request)
	{
		// Currently linked?
		Player player = session.getPlayer();
		if(player.isFacebookLinked())
		{
			// Remove Facebook data
			player.setFacebook(null);
			session.send(new FacebookStatusMessage(false, player.isLiked()));
			
			// Restore name to a random name
			String newName = session.getServer().getPlayerRegister().generateDefaultName();
			session.renamePlayer(newName);
			
			// Save immediately
			session.saveData();
		}
	}
}
