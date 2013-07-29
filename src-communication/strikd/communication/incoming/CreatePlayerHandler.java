package strikd.communication.incoming;

import strikd.sessions.Session;
import strikd.net.codec.StrikMessage;

public class CreatePlayerHandler extends MessageHandler
{
	@Override
	public String getOpcode()
	{
		return "CREATE_PLAYER";
	}
	
	@Override
	public void handle(Session session, StrikMessage request)
	{
		// Create player, login and send LoginOK as if the new credentials were routed through LoginMessageHandler
	}
}
