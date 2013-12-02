package strikd.communication.incoming;

import strikd.sessions.Session;
import strikd.communication.Opcodes;
import strikd.communication.outgoing.PlayerUnknownMessage;
import strikd.game.player.Player;
import strikd.game.player.PlayerRegister;
import strikd.net.codec.IncomingMessage;

public class DeletePlayerHandler extends MessageHandler
{
	@Override
	public Opcodes.Incoming getOpcode()
	{
		return Opcodes.Incoming.DELETE_PLAYER;
	}
	
	@Override
	public void handle(Session session, IncomingMessage request)
	{
		// The victim!
		Player player = session.getPlayer();
		
		// Need confirmation!
		String confirmToken = request.readStr();
		if(confirmToken.equals(player.getToken()))
		{
			// Issue delete
			PlayerRegister register = session.getServer().getPlayerRegister();
			register.deletePlayer(player);
			
			// Deleted!
			session.send(new PlayerUnknownMessage(player.getId()));
			
			// Disconnect!
			session.setSaveOnLogout(false);
			session.end("deleted player");
		}
	}
}
