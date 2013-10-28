package strikd.communication.incoming;

import org.bson.types.ObjectId;

import strikd.communication.Opcodes;
import strikd.communication.outgoing.CurrencyBalanceMessage;
import strikd.communication.outgoing.FacebookStatusMessage;
import strikd.communication.outgoing.ItemsMessage;
import strikd.communication.outgoing.UserInfoMessage;
import strikd.game.user.User;
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
		// Not logged in already?
		if(!session.isLoggedIn())
		{
			// Read login info
			ObjectId userId = new ObjectId(request.readStr());
			String token = request.readStr();
			String hardware = request.readStr();
			String systemVersion = request.readStr();
			
			// Correct account and password?
			User user = session.getServer().getUserRegister().findUser(userId);
			if(user == null || !token.equals(user.token))
			{
				session.end("bad login");
			}
			else
			{
				// Login OK!
				session.setUser(user, String.format("%s @ %s", hardware, systemVersion));
				
				// Push user data
				session.send(new UserInfoMessage(user));
				session.send(new FacebookStatusMessage(user.isFacebookLinked(), user.liked));
				session.send(new CurrencyBalanceMessage(user.balance));
				session.send(new ItemsMessage(user.items));
			}
		}
	}
}
