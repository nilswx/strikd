package strikd.communication.incoming;

import strikd.sessions.Session;
import strikd.net.codec.StrikMessage;

public class CancelRequestMatchHandler extends MessageHandler
{
	@Override
	public String getOpcode()
	{
		return "CANCEL_REQ";
	}
	
	@Override
	public void handle(Session session, StrikMessage request)
	{
		// Receive one or more letter coordinates
	}
}
