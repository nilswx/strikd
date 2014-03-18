package strikd.communication.incoming;

import strikd.sessions.Session;
import strikd.cluster.ServerCluster;
import strikd.cluster.ServerDescriptor;
import strikd.communication.Opcodes;
import strikd.communication.outgoing.ChallengeFailedMessage;
import strikd.communication.outgoing.ChallengeLocaleMismatchMessage;
import strikd.communication.outgoing.ChallengeOkMessage;
import strikd.communication.outgoing.ChallengeRedirectMessage;
import strikd.game.player.Player;
import strikd.net.codec.IncomingMessage;

public class ChallengePlayerHandler extends MessageHandler
{
	@Override
	public Opcodes.Incoming getOpcode()
	{
		return Opcodes.Incoming.CHALLENGE_PLAYER;
	}
	
	@Override
	public void handle(Session session, IncomingMessage request)
	{
		// Find target
		int opponentId = request.readInt();
		Player opponent = session.getServer().getPlayerRegister().findPlayer(opponentId);
		
		// Unavailable?
		if(opponent == null || !opponent.isOnline() || opponent.isInMatch())
		{
			// Failed!
			session.send(new ChallengeFailedMessage(opponent));
		}
		else
		{
			// Locale mismatch?
			Player self = session.getPlayer();
			if(!opponent.getLocale().equals(self.getLocale()))
			{
				// Give help!
				session.send(new ChallengeLocaleMismatchMessage(opponent, opponent.getLocale()));
			}
			else
			{
				// Different server?
				if(opponent.getServerId() != self.getServerId())
				{
					// Resolve server
					ServerCluster cluster = session.getServer().getServerCluster();
					ServerDescriptor remote = cluster.getServerById(opponent.getServerId());
					
					// Redirect challenger to opponent server and retry
					session.send(new ChallengeRedirectMessage(opponent, remote));
				}
				else
				{
					// Get session of target
					Session targetSession = session.getServer().getSessionMgr().getPlayerSession(opponent.getId());
					if(targetSession != null)
					{
						// Try it!
						if(session.getChallengeMgr().challenge(targetSession.getChallengeMgr()))
						{
							session.send(new ChallengeOkMessage(opponent));
						}
						else
						{
							session.send(new ChallengeFailedMessage(opponent));
						}
					}
				}
			}
		}
	}
}
