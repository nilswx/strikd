package strikd.communication.incoming;

import strikd.sessions.Session;
import strikd.net.codec.StrikMessage;

public class NopHandler extends MessageHandler
{	
	@Override
	public final String getOpcode()
	{
		return "NOP";
	}
	
	@Override
	public final void handle(Session session, StrikMessage request)
	{
		
	}
}
