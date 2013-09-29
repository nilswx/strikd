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
		if(session.getUser().isFacebookLinked())
		{
			session.getUser().fbIdentity = null;
			session.getUser().name = session.getServer().getUserRegister().generateDefaultName();
		}
	}
}
