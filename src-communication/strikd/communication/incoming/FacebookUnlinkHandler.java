package strikd.communication.incoming;

import strikd.sessions.Session;
import strikd.communication.Opcodes;
import strikd.communication.outgoing.FacebookStatusMessage;
import strikd.communication.outgoing.NameChangedMessage;
import strikd.game.user.User;
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
		User user = session.getUser();
		if(user.isFacebookLinked())
		{
			// Remove Facebook data
			user.fbIdentity = null;
			session.send(new FacebookStatusMessage(false, user.liked));
			
			// Restore name to a random name
			session.getUser().name = session.getServer().getUserRegister().generateDefaultName();
			session.send(new NameChangedMessage(session.getUser().name));
			
			// Save immediately
			session.saveData();
		}
	}
}
