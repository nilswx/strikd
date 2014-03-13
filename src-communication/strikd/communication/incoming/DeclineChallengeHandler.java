package strikd.communication.incoming;

import strikd.sessions.Session;
import strikd.communication.Opcodes;
import strikd.net.codec.IncomingMessage;

public class DeclineChallengeHandler extends MessageHandler
{
	@Override
	public Opcodes.Incoming getOpcode()
	{
		return Opcodes.Incoming.DECLINE_CHALLENGE;
	}
	
	@Override
	public void handle(Session session, IncomingMessage request)
	{
		session.getChallengeMgr().declineChallenge();
	}
}
