package strikd.communication.incoming;

import strikd.communication.Opcodes;
import strikd.communication.outgoing.NameRejectedMessage;
import strikd.communication.outgoing.NameChangedMessage;
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
		
		// Filter & validate new name
		newName = newName.replace("fuck", "");
		
		// Reject name?
		if(newName.equals("Satan"))
		{
			session.send(new NameRejectedMessage("forbidden"));
		}
		else
		{
			session.getUser().name = newName;
			session.send(new NameChangedMessage(newName));
		}
	}
}
