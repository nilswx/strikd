package strikd.communication.incoming;

import strikd.sessions.Session;
import strikd.communication.Opcodes;
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
		if(session.getUser().fbIdentity != null)
		{
			// Delete link and reset user name to random guest name
		}
	}
}
