package strikd.communication.incoming;

import strikd.sessions.Session;
import strikd.communication.Opcodes;
import strikd.game.match.MatchMaker;
import strikd.game.match.queues.PlayerQueue;
import strikd.net.codec.IncomingMessage;

public class RequestMatchHandler extends MessageHandler
{
	@Override
	public Opcodes.Incoming getOpcode()
	{
		return Opcodes.Incoming.REQUEST_MATCH;
	}
	
	@Override
	public void handle(Session session, IncomingMessage request)
	{
		if(!session.isInMatch())
		{
			MatchMaker matchMaker = null;
			PlayerQueue.Entry queueEntry = matchMaker.requestMatch(session);
			if(queueEntry != null)
			{
				session.setQueueEntry(queueEntry);
			}
		}
	}
}
