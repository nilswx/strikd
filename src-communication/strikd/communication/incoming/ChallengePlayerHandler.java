package strikd.communication.incoming;

import strikd.sessions.Session;
import strikd.cluster.ServerCluster;
import strikd.cluster.ServerDescriptor;
import strikd.communication.Opcodes;
import strikd.communication.outgoing.ChallengeDeclinedMessage;
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
			// Declined straight away!
			session.send(new ChallengeDeclinedMessage(opponentId));
		}
		else
		{
			// Locale mismatch?
			Player self = session.getPlayer();
			if(!opponent.getLocale().equals(self.getLocale()))
			{
				// Give help! (TODO: special message instead of ALERT, maybe even with a prompt to change locale)
				session.sendAlert("Hola! %s is playing in a different language. Consider changing your language to '%s' to play against %s.",
						opponent.getName(),
						opponent.getLocale(),
						opponent.getName());
			}
			else
			{
				// Different server?
				if(opponent.getServerId() != self.getServerId())
				{
					// Resolve server
					ServerCluster cluster = session.getServer().getServerCluster();
					ServerDescriptor remote = cluster.getServerById(opponent.getServerId());
					
					// Redirect to server (TODO: send special RETRY redirect with host/port instead of ALERT)
					session.sendAlert("%s is playing on server '%s', while you are on '%s'. You will be reconnected.",
							opponent.getName(),
							remote.getName(),
							cluster.getSelf().getName());
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
							// Yay! Waiting...
						}
						else
						{
							session.send(new ChallengeDeclinedMessage(opponentId));
						}
					}
				}
			}
		}
	}
}
