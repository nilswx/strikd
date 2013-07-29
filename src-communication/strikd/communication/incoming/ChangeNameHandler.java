package strikd.communication.incoming;

import strikd.communication.Opcodes;
import strikd.communication.outgoing.AlertMessage;
import strikd.net.codec.IncomingMessage;
import strikd.sessions.Session;

public class ChangeNameHandler extends MessageHandler
{
	@Override
	public Opcodes.Incoming getOpcode()
	{
		return Opcodes.Incoming.CHANGE_NAME;
	}
	
	@Override
	public void handle(Session session, IncomingMessage request)
	{
		String newName = request.readStr();
		session.send(new AlertMessage("HAHA GELUKT"));
	}
}
