package strikd.communication.incoming;

import strikd.sessions.Session;
import strikd.cluster.ServerCluster;
import strikd.cluster.ServerDescriptor;
import strikd.communication.Opcodes;
import strikd.communication.outgoing.AlertMessage;
import strikd.communication.outgoing.ChallengeMessage;
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
			session.send(new AlertMessage("That player is not available right now."));
		}
		else
		{
			// Locale mismatch?
			Player self = session.getPlayer();
			if(!opponent.getLocale().equals(self.getLocale()))
			{
				// Give help! (TODO: special message instead of ALERT, maybe even with a prompt to change locale)
				session.send(new AlertMessage(String.format("Hola! %s is playing in a different language. Consider changing your language to '%s' to play against %s.",
						opponent.getName(),
						opponent.getLocale(),
						opponent.getName())));
			}
			else
			{
				// Same server?
				if(opponent.getServerId() == self.getServerId())
				{
					// Get session of target
					Session target = session.getServer().getSessionMgr().getPlayerSession(opponent.getId());
					if(target != null)
					{
						// KOM DAN JONGEN
						target.send(new ChallengeMessage(self.getId()));
					}
				}
				else
				{
					// Resolve server
					ServerCluster cluster = session.getServer().getServerCluster();
					ServerDescriptor remote = cluster.getServerById(opponent.getServerId());
					
					// Redirect to server (TODO: send special RETRY redirect with host/port instead of ALERT)
					session.send(new AlertMessage(String.format("%s is playing on server '%s', while you are on '%s'. You will be reconnected.",
							opponent.getName(),
							remote.getName(),
							cluster.getSelf().getName())));
				}
			}
		}
	}
}
