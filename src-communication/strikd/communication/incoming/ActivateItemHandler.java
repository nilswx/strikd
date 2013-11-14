package strikd.communication.incoming;

import strikd.sessions.Session;
import strikd.communication.Opcodes;
import strikd.net.codec.IncomingMessage;

public class ActivateItemHandler extends MessageHandler
{
	@Override
	public Opcodes.Incoming getOpcode()
	{
		return Opcodes.Incoming.ACTIVATE_ITEM;
	}
	
	@Override
	public void handle(Session session, IncomingMessage request)
	{
		
	}
}
