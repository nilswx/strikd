package strikd.communication.incoming;

import strikd.sessions.Session;
import strikd.net.codec.StrikMessage;

public class StrikeLettersHandler extends MessageHandler
{
	@Override
	public String getOpcode()
	{
		return "STRIKE";
	}
	
	@Override
	public void handle(Session session, StrikMessage request)
	{
		// Remove from MatchMaker
	}
}
