package strikd.communication.incoming;

import org.bson.types.ObjectId;

import strikd.communication.Opcodes;
import strikd.communication.outgoing.CurrencyBalanceMessage;
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
		if(!session.isLoggedIn())
		{
			ObjectId userId = new ObjectId(request.readStr());
			String token = request.readStr();
			
			User user = session.getServer().getUserRegister().findUser(userId);
			if(user == null || !token.equals(user.token))
			{
				session.end("bad login");
			}
			else
			{
				session.setUser(user);
				session.send(new UserInfoMessage(user));
				session.send(new CurrencyBalanceMessage(user.currency));
				session.send(new ItemsMessage(user.items));
			}
		}
	}
}
