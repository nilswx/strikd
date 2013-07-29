package strikd.communication.incoming;

import strikd.sessions.Session;
import strikd.communication.Opcodes;
import strikd.net.codec.IncomingMessage;

public class NopHandler extends MessageHandler
{	
	@Override
	public Opcodes.Incoming getOpcode()
	{
		return Opcodes.Incoming.NOP;
	}
	
	@Override
	public final void handle(Session session, IncomingMessage request)
	{
		
	}
}
