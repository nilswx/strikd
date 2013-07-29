package strikd.communication.incoming;

import org.bson.types.ObjectId;

import strikd.communication.Opcodes;
import strikd.net.codec.IncomingMessage;
import strikd.sessions.Session;

public class LoginHandler extends MessageHandler
{
	@Override
	public Opcodes.Incoming getOpcode()
	{
		return Opcodes.Incoming.LOGIN;
	}
	
	@Override
	public void handle(Session session, IncomingMessage request)
	{
		if(!session.isLoggedIn())
		{
			ObjectId playerId = new ObjectId(request.readStr());
			String token = request.readStr();
			
			// TODO: load player data and check whether token matches
		}
	}
}
