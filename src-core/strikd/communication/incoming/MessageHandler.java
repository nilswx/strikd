package strikd.communication.incoming;

import strikd.communication.Opcodes;
import strikd.net.codec.IncomingMessage;
import strikd.sessions.Session;

public abstract class MessageHandler
{
	public abstract Opcodes.Incoming getOpcode();
	
	public abstract void handle(Session session, IncomingMessage request);
}
