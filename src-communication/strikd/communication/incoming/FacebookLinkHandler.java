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
		// Not linked yet?
		if(!session.getUser().isFacebookLinked())
		{
			// Receive Facebook link credentials
			long userId = request.readLong();
			String authToken = request.readStr();
			String authSecret = request.readStr();
			
			// Validate and link
			FacebookIdentity identity = new FacebookIdentity();
			identity.userId = userId;
			identity.authToken = authToken;
			identity.authSecret = authSecret;
			session.getUser().fbIdentity = identity;
		}
	}
}
