package strikd.communication.incoming;

import strikd.sessions.Session;
import strikd.communication.Opcodes;
import strikd.net.codec.IncomingMessage;

public class DeletePlayerHandler extends MessageHandler
{
	@Override
	public Opcodes.Incoming getOpcode()
	{
		return Opcodes.Incoming.DELETE_PLAYER;
	}
	
	@Override
	public void handle(Session session, IncomingMessage request)
	{
		String confirmToken = request.readStr();
		if(confirmToken.equals(session.getPlayer().getToken()))
		{
			session.end("deleted player");
		}
	}
}
