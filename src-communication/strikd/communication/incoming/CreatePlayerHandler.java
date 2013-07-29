package strikd.communication.incoming;

import strikd.sessions.Session;
import strikd.communication.Opcodes;
import strikd.net.codec.IncomingMessage;

public class CreatePlayerHandler extends MessageHandler
{
	@Override
	public Opcodes.Incoming getOpcode()
	{
		return Opcodes.Incoming.CREATE_PLAYER;
	}
	
	@Override
	public void handle(Session session, IncomingMessage request)
	{
		// Create player, login and send LoginOK as if the new credentials were routed through LoginMessageHandler
	}
}
