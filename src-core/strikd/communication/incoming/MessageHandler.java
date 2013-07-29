package strikd.communication.incoming;

import strikd.net.codec.StrikMessage;
import strikd.sessions.Session;

public abstract class MessageHandler
{
	public abstract String getOpcode();
	
	public abstract void handle(Session session, StrikMessage request);
}
