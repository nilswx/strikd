package strikd.communication.incoming;

import strikd.sessions.Session;
import strikd.communication.Opcodes;
import strikd.communication.outgoing.CredentialsMessage;
import strikd.game.user.User;
import strikd.net.codec.IncomingMessage;

public class CreateUserHandler extends MessageHandler
{
	@Override
	public Opcodes.Incoming getOpcode()
	{
		return Opcodes.Incoming.CREATE_USER;
	}
	
	@Override
	public void handle(Session session, IncomingMessage request)
	{
		if(!session.isLoggedIn())
		{
			User user = session.getServer().getUserRegister().newUser();
			session.send(new CredentialsMessage(user.id, user.token));
		}
	}
}
