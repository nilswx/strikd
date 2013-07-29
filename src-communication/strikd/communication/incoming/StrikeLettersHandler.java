package strikd.communication.incoming;

import strikd.sessions.Session;
import strikd.communication.Opcodes;
import strikd.net.codec.IncomingMessage;

public class StrikeLettersHandler extends MessageHandler
{
	@Override
	public Opcodes.Incoming getOpcode()
	{
		return Opcodes.Incoming.STRIKE_LETTERS;
	}
	
	@Override
	public void handle(Session session, IncomingMessage request)
	{
		// Remove from MatchMaker
	}
}
