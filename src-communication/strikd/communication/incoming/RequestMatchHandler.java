package strikd.communication.incoming;

import strikd.Server;
import strikd.sessions.Session;
import strikd.communication.Opcodes;
import strikd.communication.outgoing.ServerShuttingDownMessage;
import strikd.game.match.MatchManager;
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
		// Exit queue/match
		if(session.isInQueue())
		{
			session.exitQueue();
		}
		else if(session.isInMatch())
		{
			session.exitMatch();
		}
		
		// Can play matches?
		Server server = session.getServer();
		if(server.isShutdownMode())
		{
			String info = server.getShutdownMessage();
			session.send(new ServerShuttingDownMessage(info));
		}
		else
		{
			// Request match
			MatchManager matchMgr = server.getMatchMgr();
			PlayerQueue.Entry queueEntry = matchMgr.requestMatch(session);
			if(queueEntry != null)
			{
				session.setQueueEntry(queueEntry);
			}
			else
			{
				session.sendAlert("Could not join a queue! (locale set?)");
			}
		}
	}
}
