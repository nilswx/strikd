package strikd.communication.incoming;

import strikd.sessions.Session;
import strikd.communication.Opcodes;
import strikd.net.codec.IncomingMessage;

public class PlayerReadyHandler extends MessageHandler
{	
	@Override
	public Opcodes.Incoming getOpcode()
	{
		return Opcodes.Incoming.PLAYER_READY;
	}
	
	@Override
	public final void handle(Session session, IncomingMessage request)
	{
		if(session.isInMatch())
		{
			session.getMatchPlayer().setReady();
		}
	}
}
