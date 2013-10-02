package strikd.communication.incoming;

import strikd.sessions.Session;
import strikd.communication.Opcodes;
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
		long userId = request.readLong();
		String token = request.readStr();
			
		// Validate and link
		FacebookIdentity identity = new FacebookIdentity();
		identity.userId = userId;
		identity.token = token;
		session.getUser().fbIdentity = identity;
		
		// Save immediately
		session.saveData();
	}
}
