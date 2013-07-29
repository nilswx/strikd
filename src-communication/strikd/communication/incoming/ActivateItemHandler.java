package strikd.communication.incoming;

import strikd.sessions.Session;
import strikd.net.codec.StrikMessage;

public class ActivateItemHandler extends MessageHandler
{
	@Override
	public String getOpcode()
	{
		return "ACTIVATE";
	}
	
	@Override
	public void handle(Session session, StrikMessage request)
	{
		int itemId = request.get("id");
	}
}
