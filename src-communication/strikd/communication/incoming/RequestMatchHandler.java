package strikd.communication.incoming;

import strikd.sessions.Session;
import strikd.net.codec.StrikMessage;

public class RequestMatchHandler extends MessageHandler
{
	@Override
	public String getOpcode()
	{
		return "REQ_MATCH";
	}
	
	@Override
	public void handle(Session session, StrikMessage request)
	{
		// Add to the MatchMaker, wait for x seconds, MatchMaker can return a bot
	}
}
