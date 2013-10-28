package strikd.communication.incoming;

import strikd.sessions.Session;
import strikd.communication.Opcodes;
import strikd.net.codec.IncomingMessage;

public class ExitMatchHandler extends MessageHandler
{
	@Override
	public Opcodes.Incoming getOpcode()
	{
		return Opcodes.Incoming.EXIT_MATCH;
	}
	
	@Override
	public void handle(Session session, IncomingMessage request)
	{
		if(session.isInQueue())
		{
			session.exitQueue();
		}
		else if(session.isInMatch())
		{
			session.exitMatch();
		}
	}
}
