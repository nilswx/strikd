package strikd.communication.incoming;

import strikd.communication.Opcodes;
import strikd.communication.outgoing.NameRejectedMessage;
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
		String requestedName = request.readStr();
		
		// Filter & validate new name
		String newName = requestedName.replace("fuck", "");
		
		// Reject name?
		if(newName.equals("Satan"))
		{
			session.send(new NameRejectedMessage(requestedName, "forbidden"));
		}
		else
		{
			session.renamePlayer(newName);
		}
	}
}
