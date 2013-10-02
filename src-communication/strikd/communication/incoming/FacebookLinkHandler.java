package strikd.communication.incoming;

import strikd.sessions.Session;
import strikd.communication.Opcodes;
import strikd.communication.outgoing.FacebookStatusMessage;
import strikd.game.user.FacebookIdentity;
import strikd.net.codec.IncomingMessage;

public class FacebookLinkHandler extends MessageHandler
{
	@Override
	public Opcodes.Incoming getOpcode()
	{
		return Opcodes.Incoming.FACEBOOK_LINK;
	}
	
	@Override
	public void handle(Session session, IncomingMessage request)
	{
		// Receive latest Facebook link credentials
		FacebookIdentity identity = new FacebookIdentity();
		identity.userId = request.readLong();
		identity.token = request.readStr();
		
		// TODO: validate
		
		// Set data
		session.getUser().fbIdentity = identity;
		session.send(new FacebookStatusMessage(true));
		
		// Save immediately
		session.saveData();
	}
}
