package strikd.communication.incoming;

import org.bson.types.ObjectId;

import strikd.net.codec.StrikMessage;
import strikd.sessions.Session;

public class LoginHandler extends MessageHandler
{
	@Override
	public String getOpcode()
	{
		return "LOGIN";
	}
	
	@Override
	public void handle(Session session, StrikMessage request)
	{
		if(!session.isLoggedIn())
		{
			ObjectId playerId = new ObjectId((String)request.get("playerId"));
			String token = request.get("token");
			
			// TODO: load player data and check whether token matches
			
			StrikMessage msg = new StrikMessage("LoginOK");
			msg.set("coins", session.getPlayer().coins);
			session.send(msg);
		}
	}
}
