package strikd.communication.incoming;

import strikd.sessions.Session;
import strikd.communication.Opcodes;
import strikd.net.codec.IncomingMessage;

public class CancelRequestMatchHandler extends MessageHandler
{
	@Override
	public Opcodes.Incoming getOpcode()
	{
		return Opcodes.Incoming.CANCEL_REQUEST_MATCH;
	}
	
	@Override
	public void handle(Session session, IncomingMessage request)
	{
		if(session.isInQueue())
		{
			session.exitQueue();
		}
	}
}
