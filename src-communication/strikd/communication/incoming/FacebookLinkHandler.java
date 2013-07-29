package strikd.communication.incoming;

import strikd.sessions.Session;
import strikd.net.codec.StrikMessage;

public class FacebookLinkHandler extends MessageHandler
{
	@Override
	public String getOpcode()
	{
		return "LINK";
	}
	
	@Override
	public void handle(Session session, StrikMessage request)
	{
		if(session.getPlayer().fbIdentity == null)
		{
			long userId = request.get("userId");
			String authToken = request.get("token");
			String authSecret = request.get("secret");
			
			// Validate and link
		}
	}
}
