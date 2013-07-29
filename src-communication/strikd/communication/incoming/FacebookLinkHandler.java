package strikd.communication.incoming;

import strikd.sessions.Session;
import strikd.communication.Opcodes;
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
		if(session.getPlayer().fbIdentity == null)
		{
			long userId = request.readLong();
			String authToken = request.readStr();
			String authSecret = request.readStr();
			
			// Validate and link
		}
	}
}
