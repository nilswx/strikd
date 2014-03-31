package strikd.communication.incoming;

import strikd.sessions.Session;
import strikd.communication.Opcodes;
import strikd.net.codec.IncomingMessage;

public class RevokeChallengeHandler extends MessageHandler
{
	@Override
	public Opcodes.Incoming getOpcode()
	{
		return Opcodes.Incoming.REVOKE_CHALLENGE;
	}
	
	@Override
	public void handle(Session session, IncomingMessage request)
	{
		int playerId = request.readInt();
		
		session.getChallengeMgr().revokeChallenge(playerId);
	}
}
