package strikd.communication.incoming;

import strikd.sessions.Session;
import strikd.communication.Opcodes;
import strikd.communication.outgoing.CredentialsMessage;
import strikd.game.player.Player;
import strikd.net.codec.IncomingMessage;

public class CreatePlayerHandler extends MessageHandler
{
	@Override
	public Opcodes.Incoming getOpcode()
	{
		return Opcodes.Incoming.CREATE_PLAYER;
	}
	
	@Override
	public void handle(Session session, IncomingMessage request)
	{
		if(!session.isLoggedIn())
		{
			Player player = session.getServer().getPlayerRegister().newPlayer();
			session.send(new CredentialsMessage(player.id, player.token));
		}
	}
}
